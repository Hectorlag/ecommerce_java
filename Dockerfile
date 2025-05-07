# Usa una imagen oficial de Java 17
FROM eclipse-temurin:17-jdk

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR generado al contenedor
COPY target/ecommerce-0.0.1.jar app.jar

# Expone el puerto en el contenedor (ajustá si usás otro)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
