# Priceline
This is demo project - Android Challenge

--------------
- Retrofit - Added for api integration
- Dagger - Use the design pattern for injection
- Flexible adapter - To show the expandable view


---------------
For Api key - should store in some encrypted way.
- can store on the server side
- can use the obfuscator to, also put in hash secret and later unhash when needed 
---------------
For base url -
- I prefer to store the base url in Remote config, So we can change the url at any time without any new depoloyment.
---------------
For Handle config changes-
- I have used the viewmodel. 
- In this case of device rotaion - api is not fetching data again.
