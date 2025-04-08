document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("#registerForm").addEventListener("submit", function (event) {
        event.preventDefault();

        let username = document.getElementById("username").value.trim();
        let password = document.getElementById("password").value.trim();
        let confirmPassword = document.getElementById("confirmPassword").value.trim();
        let sex = document.querySelector('input[name="sex"]:checked').value;

        if (!username || !password || !confirmPassword) {
            alert("请填写完整信息");
            return;
        }

        if (password !== confirmPassword) {
            alert("两次输入的密码不一致");
            return;
        }

        fetch("/user/reg", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, password, sex })
        })
            .then(response => response.json())
            .then(data => {
                alert(data.message);
                if (data.status === 200) {
                    window.location.href = "login.html"; // 注册成功后跳转到登录页面
                }
            })
            .catch(error => {
                console.error("注册失败:", error);
                alert("用户名已存在！");
            });
    });
});
