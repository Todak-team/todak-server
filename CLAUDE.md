# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Plain Java 17 project with no build tool (no Maven/Gradle). Managed via IntelliJ IDEA; compiled output goes to `out/`.

## Commands

Compile from the project root:
```
javac -d out src/Main.java
```

Run:
```
java -cp out Main
```

## Coding Guidelines

Derived from [Andrej Karpathy's observations](https://x.com/karpathy/status/2015883857489522876). Bias toward caution over speed; use judgment for trivial tasks.

### 1. Think Before Coding
- State assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them — don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop, name what's confusing, and ask.

### 2. Simplicity First
- Minimum code that solves the problem. Nothing speculative.
- No features beyond what was asked.
- No abstractions for single-use code, no unrequested "flexibility".
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

### 3. Surgical Changes
- Touch only what you must. Don't improve adjacent code, comments, or formatting.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it — don't delete it.
- Remove imports/variables/functions that **your** changes made unused; leave pre-existing dead code alone.
- Every changed line should trace directly to the user's request.

### 4. Goal-Driven Execution
- Transform tasks into verifiable goals before starting.
- For multi-step tasks, state a brief plan with verify steps:
  ```
  1. [Step] → verify: [check]
  2. [Step] → verify: [check]
  ```

## Structure

- `src/` — Java source files (currently `Main.java`)
- `out/` — compiled `.class` files (git-ignored)
- `todak-server.iml` — IntelliJ IDEA module config (JDK 17, source root: `src/`)
