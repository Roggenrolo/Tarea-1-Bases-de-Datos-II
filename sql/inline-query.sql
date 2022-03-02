use Aseni;

select count(e.id) as 'entregables', c.nombre
from 
	(
		select e.*
		from
			Entregable e,
			Accion a,
			PlanGobierno pg,
			(select top (convert(int,((select count(id) from Partido)/4))) * from Partido) p
		where
			e.id_accion = a.id and
			a.id_plan_gobierno = pg.id and
			pg.id_partido = p.id
	) e,
	Canton c
where e.id_canton = c.id
group by c.nombre

select e.*
from
	Entregable e,
	Accion a,
	PlanGobierno pg,
	(select top (convert(int,((select count(id) from Partido)/4))) * from Partido) p
where
	e.id_accion = a.id and
	a.id_plan_gobierno = pg.id and
	pg.id_partido = p.id


select top ((select count(id) from Partido)/4) * from Partido

select top (1.1) * from Partido

--------------------------------------------------------------------------------------------

select count(e.id) as 'entregables', c.nombre
from 
	Entregable e,
	(
		select c.*
		from
			(
				select count(p.id) as 'cantidad_partidos', c.id
				from Entregable e, Canton c, Partido p, Accion a, PlanGobierno g
				where
					e.id_canton = c.id and
					e.id_accion = a.id and
					a.id_plan_gobierno = g.id and
					g.id_partido = p.id
				group by c.id
			) pc,
			Canton c
		where
			c.id = pc.id and
			pc.cantidad_partidos <= (select (convert(int,((select count(id) from Partido)/4))))
	)c
where e.id_canton = c.id
group by c.nombre

select c.*
from
	(
		select count(p.id) as 'cantidad_partidos', c.id
		from Entregable e, Canton c, Partido p, Accion a, PlanGobierno g
		where
			e.id_canton = c.id and
			e.id_accion = a.id and
			a.id_plan_gobierno = g.id and
			g.id_partido = p.id
		group by c.id
	) pc,
	Canton c
where
	c.id = pc.id and
	pc.cantidad_partidos <= (select (convert(int,((select count(id) from Partido)/4))))

select count(p.id) as 'cantidad_partidos', c.id
from Entregable e, Canton c, Partido p, Accion a, PlanGobierno g
where
	e.id_canton = c.id and
	e.id_accion = a.id and
	a.id_plan_gobierno = g.id and
	g.id_partido = p.id
group by c.id

select count(e.id), c.nombre
from Entregable e, Canton c
where e.id_canton = c.id
group by c.nombre