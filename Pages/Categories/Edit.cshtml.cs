using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using StreamingAdminCatalog.Models;
using StreamingAdminCatalog.Services;

namespace StreamingAdminCatalog.Pages.Categories
{
    public class EditModel : PageModel
    {
        private readonly ICategoryService _categoryService;

        public EditModel(ICategoryService categoryService)
        {
            _categoryService = categoryService;
        }

        [BindProperty]
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

        public async Task<IActionResult> OnPostAsync()
        {
            if (!ModelState.IsValid)
            {
                return Page();
            }

            var updatedCategory = await _categoryService.UpdateCategoryAsync(Category.Id, Category);
            if (updatedCategory == null)
            {
                return NotFound();
            }

            TempData["SuccessMessage"] = "Categoria atualizada com sucesso!";
            return RedirectToPage("./Index");
        }
    }
}