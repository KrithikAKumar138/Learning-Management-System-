# LMS Configuration Setup

## Database Configuration

To run this LMS application, you need to configure your database credentials:

### Step 1: Create application.properties
Copy the example configuration file:
```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

### Step 2: Update Database Credentials
Edit `src/main/resources/application.properties` and replace:
- `YOUR_DB_HOST` - Your database host (e.g., localhost or database server IP)
- `YOUR_DB_NAME` - Your database name (e.g., lms_db)
- `YOUR_DB_USERNAME` - Your database username
- `YOUR_DB_PASSWORD` - Your database password

### Example Configuration:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lms_db
spring.datasource.username=postgres
spring.datasource.password=your_secure_password_here
```

### Security Note
- **Never commit `application.properties` to Git** - it contains sensitive credentials
- The `.gitignore` file prevents accidental commits of this file
- Always keep database credentials secure and local to your environment

### Running the Application
Once configured, run:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`
