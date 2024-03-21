

function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({//将javaScript对象转化为字符串
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success: function (response) {
            if (response.code == 200){
                $("#comment_section").hide();
            }else {
                if (response.code == 504) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted){
                        window.close();
                        window.open("http://localhost:8080");
                        //window.localStorage.setItem("closable", true);
                    }
                }else{
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType:"json",
        contentType:'application/json'
    });
    console.log(questionId);
    console.log(content);
}