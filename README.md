# dojoTicTacToe
Make 2 bots play Tic Tac Toe


# How to run
- Install Android studio
- Install latest Android SDK in package manager in AS
- Run the app: Press play

# Code
Code is build using the MVVM pattern & divided in 3 parts:
Model: contains the 4 model classes: 100% test coverage. 
ViewModel: The Game class is the ViewModel: 100% test coverage
View: GameView is the only View class. It is not tested as this is not out of scope for now

Next to this there is the MainActivity which starts the app and handles Android lifecycle events

# Images in the repo
- screenshot that shows a finished 3 x 3 game
- screenshot that shows the 100% test coverage from the model and viewmodel classes

# future extensions

Adding player vs bot is fairly simple, the start code for it is comment out in the GameView.
A Player class already exists, Bot is a subclass of player.

The board can also be played on a 4x4 and 5x5 grid, but then one needs to be patient :). 
The minimax algorithm is way to slow and results in to many calculations (16x15x14x13 etc) for a full board.
Branch pruning might help, but even that will not be enough. 
Other methods (like board scoring after some moves) should be added to make the game workable on bigger than 3x3 grid.




