const queryString = window.location.search;
const userName = "admin";
const password = "crawler123";

init();

function init() {
    let index = queryString.lastIndexOf("=");
    let id = queryString.substring(index + 1, queryString.length)
    $.ajax({
        type: 'GET',
        url: `/api/medicalInstitution/detail?id=${id * 1 > 0 ? id : 0}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function (result) {
            if (result.totalItems > 0) {
                $("#code").val(result.data.code);
                $("#name").val(result.data.name);
                $("#post").val(result.data.post);
                $("#address").val(result.data.address);
                $("#phone").val(result.data.phone);
                $("#founder").val(result.data.founder);
                $("#manager").val(result.data.manager);
                $("#doctorInfo").val(result.data.doctorInfo);
                $("#bedsDepartments").val(result.data.bedsDepartments);
                $("#note").val(result.data.note);
                $("#time").val(result.data.time);
                $("#categoryName").val(result.data.categoryName);
                $("#prefectureName").val(result.data.prefectureName);
            } else {
                $("#code").val("");
                $("#name").val("");
                $("#post").val("");
                $("#address").val("");
                $("#phone").val("");
                $("#founder").val("");
                $("#manager").val("");
                $("#doctorInfo").val("");
                $("#bedsDepartments").val("");
                $("#note").val("");
                $("#time").val("");
                $("#categoryName").val("");
                $("#prefectureName").val("");
            }
        }),
        error: (function (e) {
            console.log('Error: ', e);
        })
    });
}