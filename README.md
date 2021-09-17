[![CircleCI](https://circleci.com/gh/Tochies/Statistics/tree/master.svg?style=svg)](https://circleci.com/gh/Tochies/Statistics/tree/master)

# Statistics
A quick project to track the statistics of transaction happening within a timeframe

Create new transaction
Sample url :  http://127.0.0.1:8082/transactions
Http Method : POST
Request structure : Body: { "amount":"12.3343", "timestamp":"2018-07-17T09:59:51.312Z" }  

Returns: Empty body with one of the following:  
● 201 – in case of success 
● 204 – if the transaction is older than 30 seconds 
● 400 – if the JSON is invalid 
● 422 – if any of the fields are not parsable or the transaction date is in the future  



Get all transactions within last 30 seconds
Sample url :  http://127.0.0.1:8082/statistics
Http Method : GET
No request argument

Returns : 200 Http Status code
{"sum":"1000.00", "avg":"100.53", "max":"200000.49", "min":"50.23", "count":10}  
Where: 
● sum – a BigDecimal specifying the total sum of transaction value in the last 30 seconds 
● avg – a BigDecimal specifying the average amount of transaction value in the last 30 seconds 
● max – a BigDecimal specifying single highest transaction value in the last 30 seconds 
● min – a BigDecimal specifying single lowest transaction value in the last 30 seconds 
● count – a long specifying the total number of transactions that happened in the last 30 Seconds 



Delete all transactions
Sample url : http://127.0.0.1:8082/transactions

Returns : 204 Http Status code


