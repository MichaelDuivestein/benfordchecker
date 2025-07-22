# benfordchecker
Code test
Technical task:

We want to analyse our accounts, so we are planning to implement a check using Benford’s law.

We already converted our documents to a long string which has the amounts in it, but we need to develop the tool to parse that string. Develop please a ktor web-application, with a REST api, which receives the string (containing some text and numbers) and the significance level for the chi-square test (as developers we don’t know too much about statistics, but our statistician says chi-square is going to be fine). We don’t need to implement the chi-square test, we can use a library for that.

The response should provide transparency for the decision (expected and actual distribution of digits) and the decision itself (we can assume with a certain amount of confidence that our numbers are following Benford’s law)

We need to release an MVP, but since we want to maintain it, it should be tested and we should also consider clean code principles.

Top tip is to use your personal Github account and send a link to your repo once done

