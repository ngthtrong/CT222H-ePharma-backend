# Script để thêm tất cả danh mục từ temp.json
param(
    [string]$token
)

# Function để tạo slug từ tên
function Create-Slug {
    param([string]$name)
    $slug = $name.ToLower()
    $slug = $slug -replace 'à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ', 'a'
    $slug = $slug -replace 'è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ', 'e'
    $slug = $slug -replace 'ì|í|ị|ỉ|ĩ', 'i'
    $slug = $slug -replace 'ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ', 'o'
    $slug = $slug -replace 'ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ', 'u'
    $slug = $slug -replace 'ỳ|ý|ỵ|ỷ|ỹ', 'y'
    $slug = $slug -replace 'đ', 'd'
    $slug = $slug -replace '[^a-z0-9]', '-'
    $slug = $slug -replace '-+', '-'
    $slug = $slug.Trim('-')
    return $slug
}

# Function để tạo danh mục
function Create-Category {
    param(
        [string]$name,
        [string]$parentId = $null,
        [string]$token
    )
    
    $slug = Create-Slug -name $name
    
    $body = @{
        name = $name
        slug = $slug
        description = ""
        parentCategoryId = $parentId
    } | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8081/api/v1/admin/categories" -Method POST -Headers @{"Authorization"="Bearer $token"; "Content-Type"="application/json"} -Body $body
        Write-Host "✓ Created: $name" -ForegroundColor Green
        return $response.data.id
    } catch {
        Write-Host "✗ Failed to create: $name - $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# Đọc dữ liệu từ temp.json
$jsonContent = Get-Content -Path "temp.json" -Raw | ConvertFrom-Json

# Biến để lưu ID của các danh mục đã tạo
$categoryIds = @{}

Write-Host "Starting to create categories..." -ForegroundColor Yellow

# Tạo danh mục cấp 1
foreach ($category in $jsonContent.categories) {
    Write-Host "`nCreating main category: $($category.name)" -ForegroundColor Cyan
    $mainCategoryId = Create-Category -name $category.name -token $token
    
    if ($mainCategoryId) {
        $categoryIds[$category.name] = $mainCategoryId
        
        # Tạo danh mục cấp 2
        if ($category.subcategories) {
            foreach ($subcategory in $category.subcategories) {
                Write-Host "  Creating subcategory: $($subcategory.name)" -ForegroundColor White
                $subCategoryId = Create-Category -name $subcategory.name -parentId $mainCategoryId -token $token
                
                if ($subCategoryId) {
                    $categoryIds["$($category.name)_$($subcategory.name)"] = $subCategoryId
                    
                    # Tạo danh mục cấp 3 (nếu có)
                    if ($subcategory.subcategories) {
                        foreach ($subsubcategory in $subcategory.subcategories) {
                            Write-Host "    Creating sub-subcategory: $($subsubcategory.name)" -ForegroundColor Gray
                            $subSubCategoryId = Create-Category -name $subsubcategory.name -parentId $subCategoryId -token $token
                        }
                    }
                }
            }
        }
    }
}

Write-Host "`nCategory creation completed!" -ForegroundColor Green
