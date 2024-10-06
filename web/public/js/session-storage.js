// 所有session key都在这里统一定义，可以避免多个功能使用同一个key
SESSION_ORDER = "SESSION_ORDER"
SESSION_TICKET_PARAMS= "SESSION_TICKET_PARAMS"

SessionStorage = {
    get: function (key) {
        var v = sessionStorage.getItem(key)
        if(v && typeof(v) !== "undefined" && v !== "undefined") {
            return JSON.parse(v)
        }
    },
    set: function (key, data) {
        // 将对象转为字符串，再存入
        sessionStorage.setItem(key, JSON.stringify(data))
    },
    remove: function (key) {
        sessionStorage.removeItem(key)
    },
    clearAll: function () {
        sessionStorage.clear()
    }
}