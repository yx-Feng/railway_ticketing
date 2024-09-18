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