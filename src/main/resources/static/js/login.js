document.addEventListener("DOMContentLoaded", function () {
    document.querySelector("#loginForm").addEventListener("submit", function (event) {
        event.preventDefault();

        let username = document.getElementById("username").value.trim();
        let password = document.getElementById("password").value.trim();

        console.log("发送请求:", username, password);

        fetch("/user/doLogin", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        })
            .then(response => response.json())
            .then(data => {
                console.log("后端返回:", data);
                if (data.success) {
                    window.location.href = data.redirect;
                } else {
                    alert(data.message);
                }
            })
            .catch(error => console.error("登录错误:", error));
    });
});
