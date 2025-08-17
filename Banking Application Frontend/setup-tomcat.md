# Banking Frontend - Tomcat Deployment Guide

## Option 1: Standalone Tomcat Server

### Prerequisites
- Java 8+ installed
- Apache Tomcat 9.0+ downloaded

### Quick Setup Steps

1. **Download Tomcat**
   ```bash
   # Download from https://tomcat.apache.org/
   # Extract to your preferred location
   ```

2. **Deploy Frontend**
   ```bash
   # Copy frontend files to Tomcat webapps
   cp -r banking-frontend/ /path/to/tomcat/webapps/banking/
   
   # Or create a symbolic link
   ln -s /path/to/banking-frontend /path/to/tomcat/webapps/banking
   ```

3. **Start Tomcat**
   ```bash
   # On Windows
   cd /path/to/tomcat/bin
   startup.bat
   
   # On Linux/Mac
   cd /path/to/tomcat/bin
   ./startup.sh
   ```

4. **Access Application**
   ```
   http://localhost:8080/banking/
   ```

### Configuration for Development

Create `webapps/banking/WEB-INF/web.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
    
    <display-name>Banking Frontend</display-name>
    
    <!-- Welcome files -->
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
    
    <!-- Error pages -->
    <error-page>
        <error-code>404</error-code>
        <location>/index.html</location>
    </error-page>
    
    <!-- CORS Filter (if needed) -->
    <filter>
        <filter-name>CorsFilter</filter-name>
        <filter-class>org.apache.catalina.filters.CorsFilter</filter-class>
        <init-param>
            <param-name>cors.allowed.origins</param-name>
            <param-value>*</param-value>
        </init-param>
        <init-param>
            <param-name>cors.allowed.methods</param-name>
            <param-value>GET,POST,HEAD,OPTIONS,PUT,DELETE</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CorsFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

## Option 2: Embedded Tomcat with Spring Boot

### Create a Simple Spring Boot Static Server

1. **Create new Spring Boot project**
   ```bash
   mkdir banking-frontend-server
   cd banking-frontend-server
   ```

2. **Create pom.xml**
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0">
       <modelVersion>4.0.0</modelVersion>
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.7.18</version>
           <relativePath/>
       </parent>
       
       <groupId>com.example</groupId>
       <artifactId>banking-frontend-server</artifactId>
       <version>1.0.0</version>
       <name>banking-frontend-server</name>
       
       <properties>
           <java.version>17</java.version>
       </properties>
       
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
       </dependencies>
       
       <build>
           <plugins>
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
               </plugin>
           </plugins>
       </build>
   </project>
   ```

3. **Create Main Application**
   ```java
   @SpringBootApplication
   public class FrontendServerApplication {
       public static void main(String[] args) {
           SpringApplication.run(FrontendServerApplication.class, args);
       }
   }
   ```

4. **Configure Static Resources**
   ```properties
   # application.properties
   server.port=3000
   spring.web.resources.static-locations=classpath:/static/,file:../banking-frontend/
   spring.web.resources.cache.period=0
   ```

5. **Copy Frontend Files**
   ```bash
   cp -r ../banking-frontend/* src/main/resources/static/
   ```

6. **Run the Server**
   ```bash
   mvn spring-boot:run
   # Access at http://localhost:3000
   ```

## Option 3: WAR Deployment

### Create WAR file for deployment

1. **Create build structure**
   ```
   banking-frontend-war/
   ├── src/
   │   └── main/
   │       └── webapp/
   │           ├── index.html
   │           ├── dashboard.html
   │           ├── css/
   │           ├── js/
   │           └── WEB-INF/
   │               └── web.xml
   └── pom.xml
   ```

2. **Create pom.xml for WAR**
   ```xml
   <project>
       <modelVersion>4.0.0</modelVersion>
       <groupId>com.example</groupId>
       <artifactId>banking-frontend</artifactId>
       <version>1.0.0</version>
       <packaging>war</packaging>
       
       <properties>
           <maven.compiler.source>17</maven.compiler.source>
           <maven.compiler.target>17</maven.compiler.target>
       </properties>
       
       <build>
           <finalName>banking-frontend</finalName>
           <plugins>
               <plugin>
                   <groupId>org.apache.maven.plugins</groupId>
                   <artifactId>maven-war-plugin</artifactId>
                   <version>3.2.3</version>
               </plugin>
           </plugins>
       </build>
   </project>
   ```

3. **Build and Deploy**
   ```bash
   mvn clean package
   cp target/banking-frontend.war /path/to/tomcat/webapps/
   ```

## Production Considerations

### Performance Optimization
```xml
<!-- In Tomcat's server.xml -->
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           compression="on"
           compressionMinSize="2048"
           noCompressionUserAgents="gozilla, traviata"
           compressableMimeType="text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json" />
```

### Security Headers
```xml
<!-- In web.xml -->
<filter>
    <filter-name>HttpHeaderSecurityFilter</filter-name>
    <filter-class>org.apache.catalina.filters.HttpHeaderSecurityFilter</filter-class>
    <init-param>
        <param-name>hstsEnabled</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>HttpHeaderSecurityFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

## Benefits Summary

✅ **Enterprise-grade** web server  
✅ **Better performance** than development servers  
✅ **Production-ready** configuration  
✅ **SSL/TLS support** built-in  
✅ **Load balancing** capabilities  
✅ **Monitoring and management** tools  
✅ **Java ecosystem** consistency  
✅ **Easy deployment** options 