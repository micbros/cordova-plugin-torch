window.toggleTorch = function(str, callback) {
    cordova.exec(
        callback,
        function (err) {
            callback('error');
        },
        "Torch",
        "toggleTorch",
        [str]);
}
