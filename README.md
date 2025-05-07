# Lights Out AI Solver

An interactive implementation of the classic Lights Out puzzle game with an evolving AI solver built in Java. This project features a playable interface, puzzle generator, and multiple solving strategies that will be implemented incrementally, from simple to more sophisticated approaches.

## Current Features

- Interactive 5x5 Lights Out game board
- Clean and intuitive graphical user interface built with Java Swing
- Random puzzle generator
- Basic random search solver (first implementation)
- Animated solution display
- Support for manual play and AI-assisted solving

## Development Roadmap

This project is being developed incrementally, with solving strategies being implemented in order of increasing complexity:

1. **Random Search Solver (Current)** 
   - Simple solver that tries random combinations
   - Useful for easy puzzles
   - May not find optimal solutions

2. **Brute Force Solver (Coming Soon)**
   - Will systematically try all possible combinations
   - Guaranteed to find a solution if one exists
   - May be slow for complex puzzles

3. **Pattern-Based Solver (Planned)**
   - Will use common light patterns
   - More efficient for certain configurations
   - Based on human solving strategies

4. **Advanced Solvers (Future)**
   - Mathematical approaches using linear algebra
   - Optimal solution finding
   - Performance optimizations

## Game Rules

Lights Out is played on a grid of lights, each of which can be either on or off. Clicking any light will toggle its state and the state of all adjacent lights (up, down, left, right). The goal is to turn all the lights off.

## How to Play

1. Run the game using Maven:
   ```bash
   mvn compile
   mvn exec:java -Dexec.mainClass="com.lightsout.gui.LightsOutGUI"
   ```

2. The game window will appear with a 5x5 grid of lights
3. Click on any light to toggle it and its adjacent lights
4. Use the control buttons:
   - "New Game": Start a new randomly generated puzzle
   - "Solve": Let the current AI solver attempt to solve the puzzle
   - "Reset": Stop the solver animation

## Technical Details

- Built with Java 17
- Uses Maven for dependency management
- GUI implemented with Java Swing
- Modular solver interface for easy addition of new strategies

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── lightsout/
│               ├── model/
│               │   └── Board.java
│               ├── solver/
│               │   ├── Solver.java
│               │   └── RandomSearchSolver.java
│               └── gui/
│                   └── LightsOutGUI.java
```

## Building from Source

1. Clone the repository
2. Make sure you have Java 17 and Maven installed
3. Build the project:
   ```bash
   mvn clean install
   ```

## Contributing

Contributions are welcome! Here are some ways you can contribute:
- Implement new solving strategies
- Improve the existing solvers
- Add new features to the GUI
- Fix bugs
- Improve documentation

Please feel free to submit issues and pull requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
