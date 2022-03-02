namespace queries
{
    using System;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;

    [Table("Entregable")]
    public partial class Entregable
    {
        public int id { get; set; }

        [Column(TypeName = "date")]
        public DateTime? fecha_cumplimiento { get; set; }

        public int? valor_kpi { get; set; }

        [StringLength(50)]
        public string unidad_kpi { get; set; }

        public int? id_accion { get; set; }

        public int? id_canton { get; set; }

        public virtual Accion Accion { get; set; }

        public virtual Canton Canton { get; set; }
    }
}
