package exe.weazy.intents.state

interface TestState {
    class Input : TestState
    class Loading : TestState
    class Error(val msg: String) : TestState
    class Done: TestState
    class NotRecognized : TestState
}