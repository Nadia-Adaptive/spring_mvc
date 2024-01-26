# Auction House UI

First of all, before running the UI, you will need to enable Spring Security on the BE. Otherwise, the web 
browser will not be able to connect to your local backend.
In order to do so, see instructions in the BE README file.s

You can configure the Backend URL used in the `config.ts` file in this directory.

The list of services used can be stored in the `src/service` folder. There is a generic function to create a new 
api request in the `utils.ts` file.

The corresponding types with your BE implementation can be stored in the `types.ts` file.

In order to run the UI, you first need to install all the necessary dependencies.

In your terminal, go to the `ui` folder and run
> npm install

To serve the UI you will need to either run the `dev` script from `package.json`, or run this in a dedicated terminal
> npm run dev

You should be able to open and use the UI in the default port http://localhost:5173.