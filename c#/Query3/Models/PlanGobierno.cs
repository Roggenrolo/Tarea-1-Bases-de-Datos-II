namespace queries
{
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations.Schema;

    [Table("PlanGobierno")]
    public partial class PlanGobierno
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public PlanGobierno()
        {
            Accion = new HashSet<Accion>();
        }

        public int id { get; set; }

        public int? id_partido { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<Accion> Accion { get; set; }

        public virtual Partido Partido { get; set; }
    }
}
