namespace queries
{
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    [Table("Partido")]
    public partial class Partido
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Partido()
        {
            PlanGobierno = new HashSet<PlanGobierno>();
        }

        public int id { get; set; }

        [StringLength(1000)]
        public string biografia_personal { get; set; }

        public string foto { get; set; }

        public string bandera { get; set; }

        [StringLength(100)]
        public string nombre { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<PlanGobierno> PlanGobierno { get; set; }
    }
}
