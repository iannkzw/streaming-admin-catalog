using StreamingAdminCatalog.Models;

namespace StreamingAdminCatalog.Services
{
    public interface ICategoryService
    {
        Task<IEnumerable<Category>> GetAllCategoriesAsync();
        Task<Category?> GetCategoryByIdAsync(Guid id);
        Task<Category> CreateCategoryAsync(Category category);
        Task<Category?> UpdateCategoryAsync(Guid id, Category category);
        Task<bool> DeleteCategoryAsync(Guid id);
        Task<IEnumerable<Category>> SearchCategoriesAsync(string searchTerm);
    }

    public class CategoryService : ICategoryService
    {
        private static List<Category> _categories = new List<Category>();

        static CategoryService()
        {
            // Dados de exemplo
            var category1 = new Category();
            category1.Update("Filmes", "Categoria para filmes em geral", true);
            
            var category2 = new Category();
            category2.Update("Séries", "Categoria para séries e seriados", true);
            
            var category3 = new Category();
            category3.Update("Documentários", "Documentários educativos e informativos", true);

            _categories.AddRange(new[] { category1, category2, category3 });
        }

        public Task<IEnumerable<Category>> GetAllCategoriesAsync()
        {
            var activeCategories = _categories.Where(c => c.IsActive).OrderBy(c => c.Name).AsEnumerable();
            return Task.FromResult(activeCategories);
        }

        public Task<Category?> GetCategoryByIdAsync(Guid id)
        {
            var category = _categories.FirstOrDefault(c => c.Id == id);
            return Task.FromResult(category);
        }

        public Task<Category> CreateCategoryAsync(Category category)
        {
            _categories.Add(category);
            return Task.FromResult(category);
        }

        public Task<Category?> UpdateCategoryAsync(Guid id, Category updatedCategory)
        {
            var category = _categories.FirstOrDefault(c => c.Id == id);
            if (category == null)
                return Task.FromResult<Category?>(null);

            category.Update(updatedCategory.Name, updatedCategory.Description, updatedCategory.IsActive);
            return Task.FromResult<Category?>(category);
        }

        public Task<bool> DeleteCategoryAsync(Guid id)
        {
            var category = _categories.FirstOrDefault(c => c.Id == id);
            if (category == null)
                return Task.FromResult(false);

            category.Deactivate();
            return Task.FromResult(true);
        }

        public Task<IEnumerable<Category>> SearchCategoriesAsync(string searchTerm)
        {
            if (string.IsNullOrWhiteSpace(searchTerm))
                return GetAllCategoriesAsync();

            var filteredCategories = _categories
                .Where(c => c.IsActive && 
                           (c.Name.Contains(searchTerm, StringComparison.OrdinalIgnoreCase) ||
                            (c.Description?.Contains(searchTerm, StringComparison.OrdinalIgnoreCase) ?? false)))
                .OrderBy(c => c.Name)
                .AsEnumerable();

            return Task.FromResult(filteredCategories);
        }
    }
}