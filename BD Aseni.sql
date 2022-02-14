-- Creación de Base de Datos

if not exists(select * from sys.databases where name = 'Aseni')
	create database Aseni
go

use Aseni
go

-- Creación de Tablas

if not exists (
	select *
	from sys.objects 
	where object_id = object_id(N'Partido') and type in (N'U')
) begin
	create table Partido(
		id int primary key identity(1,1),
		biografia_personal varchar(1000),
		foto varchar(max),
		bandera varchar(max),
		nombre varchar(100)
	)
end

if not exists (
	select *
	from sys.objects 
	where object_id = object_id(N'PlanGobierno') and type in (N'U')
) begin
	create table PlanGobierno(
		id int primary key identity(1,1),
		id_partido int
		foreign key (id_partido) references Partido(id)
	)
end

if not exists (
	select *
	from sys.objects 
	where object_id = object_id(N'Accion') and type in (N'U')
) begin
	create table Accion(
		id int primary key identity(1,1),
		descripcion varchar(1000),
		id_plan_gobierno int
		foreign key (id_plan_gobierno) references PlanGobierno(id)
	)
end

if not exists (
	select *
	from sys.objects 
	where object_id = object_id(N'Canton') and type in (N'U')
) begin
	create table Canton(
		id int primary key identity(1,1),
		nombre varchar(100)
	)
end

if not exists (
	select *
	from sys.objects 
	where object_id = object_id(N'Entregable') and type in (N'U')
) begin
	create table Entregable(
		id int primary key identity(1,1),
		fecha_cumplimiento date,
		valor_kpi int,
		unidad_kpi varchar(50),
		id_accion int,
		id_canton int
		foreign key (id_accion) references Accion(id),
		foreign key (id_canton) references Canton(id)
	)
end

-- Creación de Procedimientos Almacenados

if exists(
	select type_desc, type
	from sys.procedures with(nolock)
	where name = 'InsertarPartido' AND type = 'P'
)
	drop procedure InsertarPartido
go

create procedure InsertarPartido
	@biografia_personal varchar(1000),
	@foto varchar(max),
	@bandera varchar(max),
	@nombre varchar(100)
as
	if not exists(
		select id from Partido where
			biografia_personal = @biografia_personal and
			foto = @foto and
			bandera = @bandera and
			nombre = @nombre
	)begin
		insert into Partido(biografia_personal, foto, bandera, nombre)
		values(@biografia_personal, @foto, @bandera, @nombre)
		return scope_identity()
	end
go

if exists(
	select type_desc, type
	from sys.procedures with(nolock)
	where name = 'InsertarPlanGobierno' AND type = 'P'
) 
	drop procedure InsertarPlanGobierno
go

create procedure InsertarPlanGobierno
	@id_partido int
as
	insert into PlanGobierno(id_partido)
	values(@id_partido)
	return scope_identity()
go

if exists(
	select type_desc, type
	from sys.procedures with(nolock)
	where name = 'InsertarAccion' AND type = 'P'
)
	drop procedure InsertarAccion
go

create procedure InsertarAccion
	@descripcion varchar(1000),
	@id_plan_gobierno int
as
	if not exists(
		select id from Accion where
			descripcion = @descripcion and
			id_plan_gobierno = @id_plan_gobierno
	)begin
		insert into Accion(descripcion, id_plan_gobierno)
		values(@descripcion, @id_plan_gobierno)
		return scope_identity()
	end
go

if exists(
	select type_desc, type
	from sys.procedures with(nolock)
	where name = 'InsertarCanton' AND type = 'P'
)
	drop procedure InsertarCanton
go

create procedure InsertarCanton
	@nombre varchar(100)
as
	if not exists(select id from Canton where
		nombre = @nombre
	) begin
		insert into Canton(nombre)
		values(@nombre)
	end
go

if exists(
	select type_desc, type
	from sys.procedures with(nolock)
	where name = 'InsertarEntregable' AND type = 'P'
)
	drop procedure InsertarEntregable
go

create procedure InsertarEntregable
	@fecha_cumplimiento date,
	@valor_kpi int,
	@unidad_kpi varchar(50),
	@id_accion int,
	@canton varchar(100)
as
	declare @id_canton int = (select id from Canton where nombre = @canton)
	if @id_canton is null begin
		exec InsertarCanton @canton
		set @id_canton = scope_identity()
	end
	if not exists(
		select id from Entregable where
			fecha_cumplimiento = @fecha_cumplimiento and
			valor_kpi = @valor_kpi and
			unidad_kpi = @unidad_kpi and
			id_accion = @id_accion and
			id_canton = @id_canton
	) begin
		insert into Entregable(fecha_cumplimiento, valor_kpi, unidad_kpi, id_accion, id_canton)
		values(@fecha_cumplimiento, @valor_kpi, @unidad_kpi, @id_accion, @id_canton)
	end
go

if exists(
	select type_desc, type
	from sys.procedures with(nolock)
	where name = 'EntregablesCanton' AND type = 'P'
)
	drop procedure EntregablesCanton
go

create procedure EntregablesCanton
	@id_canton int
as
	select
		a.descripcion,
		e.fecha_cumplimiento,
		e.valor_kpi,
		e.unidad_kpi
	from Entregable e, Accion a, Canton c, PlanGobierno p
	where
		e.id_accion = a.id and
		e.id_canton = c.id and
		a.id_plan_gobierno = p.id and
		c.id = @id_canton
	order by a.id, p.id
go

if exists(
	select type_desc, type
	from sys.procedures with(nolock)
	where name = 'ObtenerCantones' AND type = 'P'
)
	drop procedure ObtenerCantones
go

create procedure ObtenerCantones
as
	select * from Canton
go

-- Inserción de datos

exec InsertarCanton 'Abangares';
exec InsertarCanton 'Acosta';
exec InsertarCanton 'Alajuela';
exec InsertarCanton 'Alajuelita';
exec InsertarCanton 'Alvarado';
exec InsertarCanton 'Aserrí';
exec InsertarCanton 'Atenas';
exec InsertarCanton 'Bagaces';
exec InsertarCanton 'Barva';
exec InsertarCanton 'Belén';
exec InsertarCanton 'Buenos Aires';
exec InsertarCanton 'Cañas';
exec InsertarCanton 'Carrillo';
exec InsertarCanton 'Cartago';
exec InsertarCanton 'Corredores';
exec InsertarCanton 'Coto Brus';
exec InsertarCanton 'Curridabat';
exec InsertarCanton 'Desamparados';
exec InsertarCanton 'Dota';
exec InsertarCanton 'El Guarco';
exec InsertarCanton 'Escazú';
exec InsertarCanton 'Esparza';
exec InsertarCanton 'Flores';
exec InsertarCanton 'Garabito';
exec InsertarCanton 'Goicoechea';
exec InsertarCanton 'Golfito';
exec InsertarCanton 'Grecia';
exec InsertarCanton 'Guácima';
exec InsertarCanton 'Guatuso';
exec InsertarCanton 'Heredia';
exec InsertarCanton 'Hojancha';
exec InsertarCanton 'Jiménez';
exec InsertarCanton 'La Cruz';
exec InsertarCanton 'La Unión' ;
exec InsertarCanton 'León Cortés';
exec InsertarCanton 'Liberia';
exec InsertarCanton 'Limón';
exec InsertarCanton 'Los Chiles';
exec InsertarCanton 'Matin';
exec InsertarCanton 'Montes de Oca';
exec InsertarCanton 'Montes de Oro';
exec InsertarCanton 'Mora';
exec InsertarCanton 'Moravia';
exec InsertarCanton 'Nandayure';
exec InsertarCanton 'Naranjo';
exec InsertarCanton 'Nicoya';
exec InsertarCanton 'Oreamuno' ;
exec InsertarCanton 'Orotina';
exec InsertarCanton 'Osa';
exec InsertarCanton 'Palmares';
exec InsertarCanton 'Paraíso';
exec InsertarCanton 'Parrita';
exec InsertarCanton 'Pérez Zeledón';
exec InsertarCanton 'Poás';
exec InsertarCanton 'Pococí';
exec InsertarCanton 'Puntarenas';
exec InsertarCanton 'Puriscal';
exec InsertarCanton 'Quepos';
exec InsertarCanton 'Río Cuarto';
exec InsertarCanton 'San Carlos';
exec InsertarCanton 'San Isidro';
exec InsertarCanton 'San José';
exec InsertarCanton 'San Mateo';
exec InsertarCanton 'San Pablo';
exec InsertarCanton 'San Rafael' ;
exec InsertarCanton 'San Ramón';
exec InsertarCanton 'Santa Ana';
exec InsertarCanton 'Santa Bárbara';
exec InsertarCanton 'Santa Cruz';
exec InsertarCanton 'Santo Domingo';
exec InsertarCanton 'Sarapiquí';
exec InsertarCanton 'Sarchí';
exec InsertarCanton 'Siquirre';
exec InsertarCanton 'Talamanca';
exec InsertarCanton 'Tarrazú';
exec InsertarCanton 'Tibás';
exec InsertarCanton 'Tilarán';
exec InsertarCanton 'Turrialba' ;
exec InsertarCanton 'Turrubares';
exec InsertarCanton 'Upala';
exec InsertarCanton 'Vázquez de Coronado';
exec InsertarCanton 'Zarcero';

declare @id_partido int, @id_plan_gobierno int, @id_accion int

exec @id_partido = InsertarPartido
	'El partido social va a mejorar las condiciones de vida de la sociedad',
	'*foto del partido social*',
	'*bandera del partido social*',
	'Partido social'
;
if @id_partido != 0 begin
	exec @id_plan_gobierno = InsertarPlanGobierno @id_partido;
	exec @id_accion = InsertarAccion 'Alumbrado de calles', @id_plan_gobierno;
	exec InsertarEntregable '2023-09-03', 20, 'calles', @id_accion, 'Escazú';
	exec InsertarEntregable '2022-10-20', 14, 'calles', @id_accion, 'Zarcero';
	exec InsertarEntregable '2024-03-24', 25, 'calles', @id_accion, 'Osa';
end

exec @id_partido = InsertarPartido
	'El partido del pueblo que es por y para el pueblo',
	'*foto del partido del pueblo*',
	'*bandera del partido del pueblo*',
	'Partido del pueblo'
;
if @id_partido != 0 begin
	exec @id_plan_gobierno = InsertarPlanGobierno @id_partido;
	exec @id_accion = InsertarAccion 'Reparar tuberías', @id_plan_gobierno;
	exec InsertarEntregable '2022-06-09', 200, 'tuberías', @id_accion, 'Puriscal';
	exec InsertarEntregable '2023-05-18', 120, 'tuberías', @id_accion, 'Puntarenas';
	exec InsertarEntregable '2024-02-12', 230, 'tuberías', @id_accion, 'Cartago';
end

exec @id_partido = InsertarPartido
	'El partido que por vía democrática va a mejorar la democracia',
	'*foto del partido democrático*',
	'*bandera del partido democrático*',
	'Partido democrático'
;
if @id_partido != 0 begin
	exec @id_plan_gobierno = InsertarPlanGobierno @id_partido;
	exec @id_accion = InsertarAccion 'Incremento de seguridad', @id_plan_gobierno;
	exec InsertarEntregable '2022-04-02', 40, 'cuadras', @id_accion, 'Quepos';
	exec InsertarEntregable '2022-07-21', 19, 'cuadras', @id_accion, 'San José';
	exec InsertarEntregable '2024-12-1', 38, 'cuadras', @id_accion, 'Liberia';
end

exec InsertarPartido
	'El partido va a impartir justicia',
	'*foto del partido de la justicia*',
	'*bandera del partido de la justicia*',
	'Partido de la justicia'
;