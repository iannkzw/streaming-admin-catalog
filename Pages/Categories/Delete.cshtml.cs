using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using StreamingAdminCatalog.Models;
using StreamingAdminCatalog.Services;

namespace StreamingAdminCatalog.Pages.Categories
{
    public class DeleteModel : PageModel
    {
        private readonly ICategoryService _categoryService;

        public DeleteModel(ICategoryService categoryService)
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

        public async Task<IActionResult> OnPostAsync(Guid? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var deleted = await _categoryService.DeleteCategoryAsync(id.Value);
            if (!deleted)
            {
                return NotFound();
            }

            TempData["SuccessMessage"] = "Categoria excluída com sucesso!";
            return RedirectToPage("./Index");
        }
    }
}