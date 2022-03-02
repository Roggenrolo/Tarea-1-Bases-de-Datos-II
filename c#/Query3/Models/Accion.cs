namespace queries
{
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    [Table("Accion")]
    public partial class Accion
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Accion()
        {
            Entregable = new HashSet<Entregable>();
        }

        public int id { get; set; }

        [StringLength(1000)]
        public string descripcion { get; set; }

        public int? id_plan_gobierno { get; set; }

        public virtual PlanGobierno PlanGobierno { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<Entregable> Entregable { get; set; }
    }
}
