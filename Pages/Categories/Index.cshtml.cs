using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using StreamingAdminCatalog.Models;
using StreamingAdminCatalog.Services;

namespace StreamingAdminCatalog.Pages.Categories
{
    public class IndexModel : PageModel
    {
        private readonly ICategoryService _categoryService;

        public IndexModel(ICategoryService categoryService)
        {
            _categoryService = categoryService;
        }

        public IEnumerable<Category> Categories { get; set; } = new List<Category>();

        [BindProperty(SupportsGet = true)]
        public string? SearchTerm { get; set; }

        public async Task OnGetAsync()
        {
            if (!string.IsNullOrWhiteSpace(SearchTerm))
            {
                Categories = await _categoryService.SearchCategoriesAsync(SearchTerm);
            }
            else
            {
                Categories = await _categoryService.GetAllCategoriesAsync();
            }
        }
    }
}