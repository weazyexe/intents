package exe.weazy.intents.state

interface State {
    class Loading : State
    class Loaded(val msg : String? = null) : State
    class Error(val msg : String? = null) : State
}