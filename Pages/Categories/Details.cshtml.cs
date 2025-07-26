using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using StreamingAdminCatalog.Models;
using StreamingAdminCatalog.Services;

namespace StreamingAdminCatalog.Pages.Categories
{
    public class DetailsModel : PageModel
    {
        private readonly ICategoryService _categoryService;

        public DetailsModel(ICategoryService categoryService)
        {
            _categoryService = categoryService;
        }

        public Category Category { get; set; } = default!;

        public async Task<IActionResult> OnGetAsync(Guid? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var category = await _categoryService.GetCategoryByIdAsync(id.Value);
            if (category == null)
            {
                return NotFound();
            }

            Category = category;
            return Page();
        }
    }
}