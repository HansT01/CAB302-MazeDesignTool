# Set up guide

This application uses mariadb to store data. 
The 'admin' account will be able to access and modify all mazes, 
whereas all other accounts created will only have access to mazes 
created by the corresponding accounts.

Step 1: Generate the distribution files from the build.xml

Step 2: Navigate to the dist folder.

Step 3: Change the db.props file according to your needs using this template:
jdbc.url=jdbc:mariadb://localhost:3306
jdbc.schema=cab302
jdbc.username=root
jdbc.password=password

Step 4: Run launch.bat

Step 5: Register an administrator account with the username 'admin'




