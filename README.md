# bcradata API

## Descripción del proyecto
Este proyecto espara obtener datos de alguna variables del BCRA y retornarlas para un futuro Forntend donde se muestren las mismas tambien se entregara las cotizaciones de los dolares Oficial, Blue, Bolsa, CCL, Mayorista, Cripto y Tarjeta/Turista

## Características clave
Para simplificar el desarrollo usare Lombok para lo que seria generacion automaticas de constructores, getters y setters

Este proyecto estara realizandose por etapas:

    - Etapa 1 Creacion da API base:
        - En esta etapa se desarollara lo siguiente:
            1. POJO de serializacion
            2. Servicios de consultar las API de terceros (BCRA y DolarAPI)
            3. Colecciones para almacenar los datos
            4. Procesos de serializacion de datos
            5. Srvicios para exponer los datos ya procesados
    - Etapa 2 Integracion con JPA:
        - En esta etapa se desarollara lo siguiente:
            1. Variables de entorno
            2. Docker file de la BD
            3. DER
            4. Entidades
            5. Relaciones
    - Etapa 3 Integracion con Spring Security:
        - En esta etapa se desarollara lo siguiente:
            1. Configuracion de Spring Security
            2. Configuracion de JWT
            3. Configuracion de Roles
            4. Configuracion de Permisos 


## Uso
### Ejecucion del proyecto de forma Local

Clonar el proyecto

```bash
  git clone https://github.com/devgaf/bcradata.git
```

Ir al directorio del proyecto

```bash
  cd bcradata
```

Instalar depndencias

```bash
  mvn clean install
```

Poner en marcha el servidor

```bash
  mvn spring-boot:run
```
---
---

## Contribuciones
Son bienvenidas todas las contribuciones al proyecto, si deseas contribuir por favor sigue los siguientes pasos:

    - Si se trata de una mejora o correccion de codigo, por favor, avisame que parte del codigo estas modificando y porque y levanta un issue
    - Si se trata de una nueva funcionalidad, por favor, crea una peticion de modificacion comentandome que cambios deseas realizar y explicame el porque, ya que tengo otros proyectos que pueden estar relacionados y no quiero que se rompan ni ser redundante

## Autor
#### Hola, soy Gaston Fernandez👋 
> *Desarrollador de sistemas*

Puede vistiar mi LinkedIn (https://www.linkedin.com/in/gastonfdz)
### 🛠 Skills
- Java 
    - Spring Boot
    - JPA
- SQL
- Angular 
- Bootstrap