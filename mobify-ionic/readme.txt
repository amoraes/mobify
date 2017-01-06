*** INITIAL SETUP INSTRUCTIONS ***
- Install nvm to manage your different npm packages
curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.0/install.sh | bash

- Remove any previous installed npm modules (optional)
sudo npm uninstall -g a_module

- Install the last nodejs version
sudo nvm install node

- Install ionic and cordova
sudo npm install -g ionic@latest cordova@latest

- Restore configuration based on package.json
ionic state restore

- Install npm modules
npm install

- Run the app on browser
ionic serve

- To avoid cross-origin problems when testing in the browser start Chrome using the command below
chrome --disable-b-security --user-data-dir

