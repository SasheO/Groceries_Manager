# Groceries_Manager

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Enables user to manage shopping list, pantry records and recipes in one app.

### App Evaluation
- **Category:** Productivity
- **Mobile:** This app would be primarily developed for a mobile device because of its portability and possible push notifications (implemented as a stretch feature).
- **Story:** Keeps all the user needs for groceries and cooking in one location.
- **Market:** Anyone can use this app, particularly adults who manage their own groceries
- **Habit:** This app should be used often as it manages user needs for daily living (cooking, getting groceries)
- **Scope:** Pretty limited in scope, could possibly expand to more social app for sharing recipes (and not just making personal recipes or getting recipes from an API)

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User must sign up/in to use app
* User can view and edit (add and delete) grocery shopping list on app
    * Each item has a "delete" button and a "bought" button to move item to pantry list
  * User can move items from grocery shopping list to pantry directly
* User can view and edit (add and delete) of ingredients in their pantry list
    * Each item has a delete button to remove list from pantry
* User can search recipes gotten from an online recipe database e.g. [Edamam](https://developer.edamam.com/edamam-docs-recipe-api)


**Optional Nice-to-have Stories**
* User can save and unsave recipes
* User can view saved recipes
* User can create own recipes which are automatically saved
* User can apply filters when searching recipes (like vegan, vegetarian, gluten-free)
* User can input expiration dates/freshness limits on groceries and receive push notifications closer to said date to prevent wastage
    * Items in pantry must be clickable
* User can select things in grocery list and implement recipe search directly


### 2. Screen Archetypes

* Register
* Login
   * Upon download of the application, user is prompted to log in.
* Stream
   * User can view grocery shopping list and delete item
* Create
   * User can add new item to grocery shopping list
* Stream
   * User can view pantry list and delete item
* Create
   * User can add new item to pantry list
  * (stretch) User can edit information on pantry item after it is created e.g. expiration dates
* Stream
  * User can search recipes gotten from online recipe database
* Stream (optional)
    * User can view their saved recipes and username
* Detail
    * User can view recipes in detail
* Create (optional)
    * User can create own recipes


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Grocery List
* Pantry list
* Saved recipes (optional)
* Recipe search

**Flow Navigation** (Screen to Screen)

* Login screen
   * => Register screen, if user does not already have account
   * => Grocery list
 * Register screen
   * => Grocery list
* Grocery List
   * => Create grocery item
   * => (stretch) search for recipe with selected grocery item
* Grocery item
  * => Grocery List 
* Pantry List
  * => Create pantry item
* Pantry item
  * => Pantry List
* Saved recipes
  * => Create new recipe
* New Recipe
  * => Saved recipes


## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="IMG-3501.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
