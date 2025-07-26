using System.ComponentModel.DataAnnotations;

namespace StreamingAdminCatalog.Models
{
    public class Category
    {
        public Guid Id { get; set; }

        [Required(ErrorMessage = "O nome é obrigatório")]
        [StringLength(100, ErrorMessage = "O nome deve ter no máximo 100 caracteres")]
        public string Name { get; set; } = string.Empty;

        [StringLength(500, ErrorMessage = "A descrição deve ter no máximo 500 caracteres")]
        public string? Description { get; set; }

        public bool IsActive { get; set; } = true;

        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }
        public DateTime? DeletedAt { get; set; }

        public Category()
        {
            Id = Guid.NewGuid();
            CreatedAt = DateTime.UtcNow;
            UpdatedAt = DateTime.UtcNow;
        }

        public void Activate()
        {
            IsActive = true;
            DeletedAt = null;
            UpdatedAt = DateTime.UtcNow;
        }

        public void Deactivate()
        {
            IsActive = false;
            DeletedAt = DateTime.UtcNow;
            UpdatedAt = DateTime.UtcNow;
        }

        public void Update(string name, string? description, bool isActive)
        {
            Name = name;
            Description = description;
            
            if (isActive && !IsActive)
            {
                Activate();
            }
            else if (!isActive && IsActive)
            {
                Deactivate();
            }
            else
            {
                UpdatedAt = DateTime.UtcNow;
            }
        }
    }
}