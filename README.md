Code Sample


Implement a solution to the following in Java. Feel free to pull in any libraries / frameworks / etc that you prefer to use in your normal day to day. We are looking for code that is clean and well-factored.


You do not need to provide any data persistence for this sample. Use in-memory data structures instead. Please include some way of running automated tests that show your application is correct. Your integration tests should run over HTTP.


The app should consist of a REST API. Do not spend anytime creating a GUI.


Here are the requirements:


Consider a grocery store where items have prices per unit but also volume prices. For example, doughnuts may be $1.25 each or 3 for $3 dollars.


Implement a point-of-sale scanning API that accepts an arbitrary ordering of products (similar to what would happen when you are actually at a checkout line) then returns the correct total price for an entire shopping cart based on the per unit prices or the volume prices as applicable.


For the purposes of this exercise, assume the following products exist in the system (no need to provide an API to add new products, you can hardcode this data):


Product Code	Price
------------	-----
A			$1.25 each or 3 for $3.00
B			$4.25
C			$1.00 or $5 for a six pack
D			$0.75


Your REST API should support the following operations:


Scan a product. This scans a single Product Code
Calculate total. This returns the total price for everything that has been scanned, taking into account the price rules defined above.


For this sample, you do not have to worry about keeping track of different carts/sessions. Just assume one long running checkout process.


Here are the minimal inputs you should use for your test cases. Please write tests for these inputs and make sure they are passing.


Scan these items in this order: ABCDABA; Verify the total price is $13.25.
Scan these items in this order: CCCCCCC; Verify the total price is $6.00.
Scan these items in this order: ABCD; Verify the total price is $7.25.