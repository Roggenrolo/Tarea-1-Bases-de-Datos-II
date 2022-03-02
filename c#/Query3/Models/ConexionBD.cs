using System.Data.Entity;

// Modelos del ORM y conexión a base de datos

namespace queries{
    public partial class ConexionBD : DbContext{
        public ConexionBD() : base("name=ConexionBD"){}

        public virtual DbSet<Accion> Accion { get; set; }
        public virtual DbSet<Canton> Canton { get; set; }
        public virtual DbSet<Entregable> Entregable { get; set; }
        public virtual DbSet<Partido> Partido { get; set; }
        public virtual DbSet<PlanGobierno> PlanGobierno { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder){
            modelBuilder.Entity<Accion>()
                .Property(e => e.descripcion)
                .IsUnicode(false);

            modelBuilder.Entity<Accion>()
                .HasMany(e => e.Entregable)
                .WithOptional(e => e.Accion)
                .HasForeignKey(e => e.id_accion);

            modelBuilder.Entity<Canton>()
                .Property(e => e.nombre)
                .IsUnicode(false);

            modelBuilder.Entity<Canton>()
                .HasMany(e => e.Entregable)
                .WithOptional(e => e.Canton)
                .HasForeignKey(e => e.id_canton);

            modelBuilder.Entity<Entregable>()
                .Property(e => e.unidad_kpi)
                .IsUnicode(false);

            modelBuilder.Entity<Partido>()
                .Property(e => e.biografia_personal)
                .IsUnicode(false);

            modelBuilder.Entity<Partido>()
                .Property(e => e.foto)
                .IsUnicode(false);

            modelBuilder.Entity<Partido>()
                .Property(e => e.bandera)
                .IsUnicode(false);

            modelBuilder.Entity<Partido>()
                .Property(e => e.nombre)
                .IsUnicode(false);

            modelBuilder.Entity<Partido>()
                .HasMany(e => e.PlanGobierno)
                .WithOptional(e => e.Partido)
                .HasForeignKey(e => e.id_partido);

            modelBuilder.Entity<PlanGobierno>()
                .HasMany(e => e.Accion)
                .WithOptional(e => e.PlanGobierno)
                .HasForeignKey(e => e.id_plan_gobierno);
        }
    }
}