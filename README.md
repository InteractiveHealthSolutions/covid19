# To setup Spring boot app
In order to setup Lombok for your IDE, download and run lombok.jar from https://projectlombok.org

# To create React web app, install NodeJS and execute the following commands
$ npm install -g create-react-app
$ create-react-app app

# Add required libraries to React app
$ cd app
$ npm add bootstrap@4.1.2 react-cookie@2.2.0 react-router-dom@4.3.1 reactstrap@6.3.0

# Export the Spring web app as web archive
$ mvn clean install package
# The deployable war build will be available in target directory

