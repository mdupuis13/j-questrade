# j-Questrade

j-Questrade is a [Questrade API](https://www.questrade.com/api) wrapper written in Java. It aims to simplify access to Questrade's API.

## Features
* Automatic access token renewal
* Methods for all account calls and most market calls
* Ability to interact with Questrade API responses as Java objects

<!--- Commented until the interface is minimally functional
## Example Usage

```java
Questrade q = new Questrade(refreshToken);

try {
	q.authenticate(); // This must be called before making API requests
	
	Account[] accs     = q.getAccounts();  // Get all accounts
	ZonedDateTime time = q.getTime();      // Get server time
	
	ZonedDateTime startTime = ZonedDateTime.now().minusMonths(1);
	ZonedDateTime endTime   = ZonedDateTime.now();
	
	// Get all orders for the first account in the last month
	Order[] orders = q.getOrders(accs[0].getNumber(), startTime, endTime);
	
} catch (RefreshTokenException e) { 
	// Prompt user to enter another refresh token
}
```
-->

## Installation
Just use it as a [git](https://git-scm.com/book/en/v2/Git-Tools-Submodules) [submodule](https://www.atlassian.com/git/tutorials/git-submodule)

## Documentation
* [Javadoc] -- to come
* [GitHub Wiki] -- to come

## Disclaimers

* This library is not affiliated, maintained, authorized, or endorsed by Questrade.

* This library has not been heavily tested. Use at your own risk.