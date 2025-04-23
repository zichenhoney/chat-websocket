let currentChatUser = null;
let socket = null;
let currentUser = null;

// 页面加载后
document.addEventListener("DOMContentLoaded", function () {
    fetch("/user/currentUser")
        .then(response => response.json())
        .then(data => {
            if (data.username) {
                currentUser = data.username;
                sessionStorage.setItem("username", currentUser);
                document.getElementById("currentUserSpan").innerText = "当前用户: " + currentUser;
                loadContacts();
                initWebSocket();  // 初始化 WebSocket
            } else {
                alert("请先登录！");
                window.location.href = "http://localhost:8080";
            }
        })
        .catch(error => console.error("获取当前用户失败:", error));
});

// 初始化 WebSocket 连接
function initWebSocket() {
    socket = new WebSocket("ws://localhost:8080/chat");

    socket.onopen = function () {
        console.log("WebSocket 连接成功");
    };

    socket.onmessage = function (event) {
        console.log("收到消息:", event.data);
        let msg = null;
        try {
            msg = JSON.parse(event.data);
        } catch(e) {
            console.error("JSON解析错误:", e);
            return;
        }
        // 如果消息属于当前会话，则追加消息
        if ( (msg.senduser === currentChatUser && msg.receiveuser === currentUser) ||
            (msg.senduser === currentUser && msg.receiveuser === currentChatUser) ) {
            appendMessage(msg);
        } else {
            // 其他会话的消息可以选择提示有新消息
            console.log("来自其他会话的消息:", msg);
        }
    };

}




// 统一：加载联系人详细信息
function loadContacts() {
    let currentUser = sessionStorage.getItem("username");
    if (!currentUser) {
        console.error("当前用户未登录，无法加载联系人");
        return;
    }
    let url = "/friends?user_name=" + currentUser;
    console.log("请求联系人列表 URL:", url);
    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log("联系人接口返回数据:", data);
            let contactsList = document.getElementById("contacts");
            contactsList.innerHTML = "";  // 清空联系人列表

            // 检查后端返回是否含有 friends 数组
            if (!data.friends || data.friends.length === 0) {
                contactsList.innerHTML = "<li>暂无联系人</li>";
                return;
            }

            // 遍历每个好友对象，数据格式应为 {id, name, avatar, lastMessage}
            data.friends.forEach(friend => {
                console.log("处理好友数据:", friend);  // 打印每个好友的详细信息

                // 创建 <li> 元素
                let li = document.createElement("li");
                li.classList.add("contact");

                // 创建头像 <img>
                let avatarImg = document.createElement("img");

                // 拼接头像路径，假设使用用户 ID 来构建头像路径
                avatarImg.src = friend.avatar
                    ? `/avatar/${friend.avatar}`  // 使用存储的 avatar 字段作为文件名
                    : `/avatar/avatar_${friend.id}.jpg`;  // 使用用户 ID 来构建头像路径

                avatarImg.classList.add("contact-avatar");

                // 创建联系人名称 <span>
                let nameSpan = document.createElement("span");
                nameSpan.classList.add("contact-name");
                nameSpan.textContent = friend.name;  // 显示联系人名称

                // 创建最后一条消息 <span>（可选）
                let lastMsgSpan = document.createElement("span");
                lastMsgSpan.classList.add("contact-last-message");
                lastMsgSpan.textContent = friend.lastMessage || "";

                // 创建信息容器，把名称和最后消息组合
                let infoDiv = document.createElement("div");
                infoDiv.classList.add("contact-info");
                infoDiv.appendChild(nameSpan);
                infoDiv.appendChild(lastMsgSpan);

                // 将头像和信息容器添加到 <li>
                li.appendChild(avatarImg);
                li.appendChild(infoDiv);

                // 绑定点击事件：点击后打开聊天窗口
                li.addEventListener("click", function () {
                    openChat(friend.name);  // 使用 friend.name（或者 friend.id）
                });

                contactsList.appendChild(li);
            });
        })
        .catch(error => console.error("加载联系人失败:", error));
}



// 打开聊天窗口并加载聊天记录
function openChat(friendName) {
    currentChatUser = friendName;
    document.getElementById("chatTitle").innerText = "与 " + friendName + " 的聊天";
    loadChatHistory(friendName);
}


// 加载聊天记录
function loadChatHistory(friendName) {
    let currentUser = sessionStorage.getItem("username");
    if (!currentUser) {
        console.error("当前用户未登录");
        return;
    }
    let url = `/chat/history?user=${currentUser}&friend=${friendName}`;
    console.log("加载聊天记录 URL:", url);
    fetch(url)
        .then(response => response.json())
        .then(result => {
            console.log("聊天记录接口返回:", result);
            if (result.code !== 200) {
                console.error("获取聊天记录失败:", result.msg);
                document.getElementById("chatMessages").innerHTML = "<p>加载聊天记录失败</p>";
                return;
            }
            let messages = result.data; // 假设返回格式为 { code:200, data: [...] }
            let chatMessages = document.getElementById("chatMessages");
            chatMessages.innerHTML = ""; // 清空记录
            if (!messages || messages.length === 0) {
                chatMessages.innerHTML = "<p>暂无聊天记录</p>";
                return;
            }
            messages.forEach(msg => {
                let div = document.createElement("div");
                div.classList.add("message");
                // 根据消息发送者判断样式
                if (msg.senduser === currentUser) {
                    div.classList.add("sent");
                } else {
                    div.classList.add("received");
                }
                // 使用 detail 和 createdate 字段
                div.innerHTML = `<p>${msg.detail}</p><span class="msgTime">${msg.createdate}</span>`;
                chatMessages.appendChild(div);
            });
            chatMessages.scrollTop = chatMessages.scrollHeight;
        })
        .catch(error => console.error("加载聊天记录失败:", error));
}

function appendMessage(msg) {
    // 获取聊天显示容器
    const chatMessages = document.getElementById("chatMessages");
    // 创建一个消息 div
    const div = document.createElement("div");
    div.classList.add("message");

    // 根据发送者来设置不同的样式
    if (msg.senduser === sessionStorage.getItem("username")) {
        div.classList.add("sent");
    } else {
        div.classList.add("received");
    }

    // 创建消息内容和发送时间的 HTML
    let timeText = msg.createdate ? new Date(msg.createdate).toLocaleString() : new Date().toLocaleString();
    div.innerHTML = `<p>${msg.detail}</p><span class="msgTime">${timeText}</span>`;

    // 追加到聊天记录容器中
    chatMessages.appendChild(div);
    // 滚动到底部
    chatMessages.scrollTop = chatMessages.scrollHeight;
}


// 发送消息
function sendMessage() {
    if (!currentChatUser) {
        alert("请先选择好友再发送消息！");
        return;
    }

    let msgInput = document.getElementById("messageInput");
    let content = msgInput.value.trim();
    if (!content) return;

    let currentUser = sessionStorage.getItem("username");

    const message = {
        senduser: currentUser,
        receiveuser: currentChatUser,
        detail: content,
        createdate: null,
        status: null
    };



    // 通过 WebSocket 发送消息
    if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(message));
    } else {
        alert("WebSocket 未连接，请刷新页面！");
        return;
    }

    // 立即显示发送的消息
    let chatMessages = document.getElementById("chatMessages");
    let div = document.createElement("div");
    div.classList.add("message", "sent");
    div.innerHTML = `<p>${content}</p><span class="msgTime">${new Date().toLocaleString()}</span>`;
    chatMessages.appendChild(div);
    chatMessages.scrollTop = chatMessages.scrollHeight;

    msgInput.value = "";
}


// 绑定按钮事件
document.getElementById("sendBtn").addEventListener("click", sendMessage);
document.getElementById("confirmAddBtn").addEventListener("click", addFriend);
document.getElementById("modalClose").addEventListener("click", function() {
    document.getElementById("addFriendModal").style.display = "none";
});
document.getElementById("chatBtn").addEventListener("click", function() {
    document.getElementById("friendList").style.display = "block";
    document.getElementById("chatArea").style.display = "flex";
    document.getElementById("addFriendModal").style.display = "none";
    this.classList.add("active");
    document.getElementById("addFriendBtn").classList.remove("active");
});
document.getElementById("addFriendBtn").addEventListener("click", function() {
    document.getElementById("friendList").style.display = "none";
    document.getElementById("chatArea").style.display = "none";
    document.getElementById("addFriendModal").style.display = "block";
    this.classList.add("active");
    document.getElementById("chatBtn").classList.remove("active");
});

//添加好友函数
function addFriend() {
    let currentUser = sessionStorage.getItem("username");
    let friendName = document.getElementById("friendInput").value.trim();
    if (!currentUser) {
        alert("当前用户未登录，请重新登录！");
        return;
    }
    if (!friendName) {
        alert("请输入好友用户名！");
        return;
    }
    if (currentUser === friendName) {
        alert("不能添加自己为好友！");
        return;
    }

    fetch("/friends/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ user_name: currentUser, friend_name: friendName })
    })
        .then(response => response.json())
        .then(result => {
            console.log("添加好友返回:", result);
            if (result && result.code === 200) {
                alert(result.msg || "好友添加成功");
                document.getElementById("addFriendModal").style.display = "none";
                document.getElementById("friendInput").value = "";
                //loadContacts();      // 重新加载联系人列表

            } else {
                alert(result.msg || "添加好友失败");
            }
        })

}

