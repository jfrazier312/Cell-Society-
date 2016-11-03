Austin Gartside
9/30/16

###Design Issue
There were two major design issues that I changed in my code. The first was for simulations that required a randomized set-up, the createGrid() method was too long
and dense. The second was that although each of my simulations was an extensions of a superclass for simulations, I was still getting the grid and changing it
manually. The issue was that I wasn't hiding the grid behind the scenes, rather I was accessing it and changing it directly in my simulation classes. 

For the first issue, I refactored by extracting the code to create the list of random options into a separate method. This made the code more readable because
it was not as dense, and the logic was more clear as well as the variable naming. For the second change, I created getters and setters in the simulation super class
GridCell, which allowed me to access cells in the grid without actually passing the grid to the simulation. This change was implemented in the createGrid() and
updateGrid() methods in each simulation. This is better because it allows me to use anything for the grid rather than a 2D array. All that would be required for a
change would be editing the superclass, but no change actually happens in the simulation because the implementation of the grid is decided in the superclass. 

Ignore anything that has to do with the SugarSimulation, as that is unrelated to the refactoring:
https://git.cs.duke.edu/CompSci308_2016Fall/cellsociety_team19/commit/c9ab2206e5544089ec4eeda2cf6a0522c4137a93