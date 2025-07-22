Technical task:

We want to analyse our accounts, so we are planning to implement a check using Benford’s law.

We already converted our documents to a long string which has the amounts in it, but we need to develop the tool to parse that string. Develop please a ktor web-application, with a REST api, which receives the string (containing some text and numbers) and the significance level for the chi-square test (as developers we don’t know too much about statistics, but our statistician says chi-square is going to be fine). We don’t need to implement the chi-square test, we can use a library for that.

The response should provide transparency for the decision (expected and actual distribution of digits) and the decision itself (we can assume with a certain amount of confidence that our numbers are following Benford’s law)

We need to release an MVP, but since we want to maintain it, it should be tested and we should also consider clean code principles.

Top tip is to use your personal Github account and send a link to your repo once done


------------------------------------------------------------------------------------------------------------------------
# Notes
- This is the first project that I've made with Ktor and Koin (normally I use Spring Boot). Hopefully it's up to standard.
- I'm unfamiliar with Benford's Law and chi-square distribution (Intro to Machine Learning class was a very long time ago). The above spec that I've received contains no examples of input data beyond a vague mention of a String and a significance number. As such, I'm guessing the structure of the input and output data based on my research of the subject and the Apache Commons Math functions. If the input and/or output is not what was expected, please send me an example so that it can be corrected.
- Due to time constraints, authentication, proper logging, and OpenAPI documentation will be ignored.

# Assumptions
- The amounts will be separated by a space.
- There will be non-numeric strings mixed in with the amounts, and they need to be ignored.
- The numbers may be decimals, which will be separated by the `.` character.
- The numbers will not be formatted. Digits separated by characters such as a comma or a space will be treated as separate numbers.
- Numbers containing only zeroes will be ignored.
