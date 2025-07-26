using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using StreamingAdminCatalog.Models;
using StreamingAdminCatalog.Services;

namespace StreamingAdminCatalog.Pages.Categories
{
    public class CreateModel : PageModel
    {
        private readonly ICategoryService _categoryService;

        public CreateModel(ICategoryService categoryService)
        {
            _categoryService = categoryService;
        }

        [BindProperty]
        public Category Category { get; set; } = new Category();

        public void OnGet()
        {
            // Initialize with default values
            Category.IsActive = true;
        }

        public async Task<IActionResult> OnPostAsync()
        {
            if (!ModelState.IsValid)
            {
                return Page();
            }

            await _categoryService.CreateCategoryAsync(Category);

            TempData["SuccessMessage"] = "Categoria criada com sucesso!";
            return RedirectToPage("./Index");
        }
    }
}