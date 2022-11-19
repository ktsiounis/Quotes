# Quotes

## Introduction :hand:

This app for me is a good implementation of my latest experience with Flows, MVVM (with repository pattern) and new Unit Test techniques. Quotes is an app that has a list of quotes from a BE implementation and has the features of editing, deleteting and adding new quotes. It also has a feature of getting a random quote and it's the same for a whole day. At the end of the day, the random quote will change.

## Screenshots :framed_picture:

| Quotes List | Quotes Details |
| --- | --- |
| <img src="/screenshots/quotes_list.png"  height="500" /> | <img src="/screenshots/quote_details.png"  height="500" /> |
| Random Quote | New Quote |
| <img src="/screenshots/random_quote.png"  height="500" /> | <img src="/screenshots/new_quote.png"  height="500" /> |
| Edit Quote |
| <img src="/screenshots/edit_quote.png"  height="500" /> |

## Testing :test_tube:

For the Unit testing I used [Mockk](https://mockk.io/) for mocking dependencies, [Turbine](https://github.com/cashapp/turbine) for Flows testing and [kotest](https://github.com/kotest/kotest) for more comprehensive matchers.
