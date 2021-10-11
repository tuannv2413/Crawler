let page = 0;
let limit = 10;
let name = "";
let valueRadio = 0;
let listFile = [];
let formData = new FormData();
const userName = "admin";
const password = "crawler123";
init();

function init() {
    $.ajax({
        type: 'GET',
        url: `/api/prefecture?page=${page}&limit=${limit}&name=${name}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function (result) {
            if (result.data.length > 0) {
                let i = 1;
                let attribute = "";
                for (const value of result.data) {
                    attribute +=
                        `<tr id="${value.id}">
                        <td>${i}</td>
                        <td style="text-align: center">${value.name}</td>
                        <td style="text-align: center">${value.nameKana}</td>
                        <td style="text-align: center">
                            <button type="button" class="btn-action" id="btnAction" onclick="handleDelete(${value.id})">Action</button>
                        </td>
                    </tr>`;
                    i++;
                }
                ;
                $("#table_tbody").html(attribute);
                let attribute2 = `<li><a id="previous" onclick="loadData(0)">«</a></li>`;
                for (let j = 0; j < result.totalPage; j++) {
                    if (j === page) {
                        attribute2 += `<li><a id="active${j}" onclick="loadData(${j})" class="active">${j + 1}</a></li>`;
                    } else {
                        attribute2 += `<li><a id="active${j}" onclick="loadData(${j})">${j + 1}</a></li>`;
                    }
                }
                $("#pagination").html(attribute2);
                $("#pagination").append(`<li><a id="next" onclick="loadData(${result.totalPage - 1})">»</a></li>`);
                $("#spin").hide();
            } else {
                $("#spin").hide();
                $("#table_tbody").hide();
                $("#div-pag").hide();
                $("#div_nodata").css("display", "block");
            }
        }),
        error: (function (e) {
            alert('Error');
        })
    });
}

function loadData(page) {
    $("#table_tbody").css("opacity", "0.5");
    $("#div-pag").css("opacity", "0.5");
    // $("#spin").show();
    $("a").removeClass("active");
    $("#active" + page).addClass("active");
    $.ajax({
        type: 'GET',
        url: `/api/prefecture?page=${page}&limit=${limit}&name=${name}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function (result) {
            let i = 1;
            let attribute = "";
            for (const value of result.data) {
                attribute +=
                    `<tr id="${value.id}">
                        <td>${i}</td>
                        <td style="text-align: center">${value.name}</td>
                        <td style="text-align: center">${value.nameKana}</td>
                        <td style="text-align: center">
                            <button type="button" class="btn-action" id="btnAction" onclick="handleDelete(${value.id})">Action</button>
                        </td>
                    </tr>`
                i++;
            }
            $("#table_tbody").html(attribute);
            $("#table_tbody").css("opacity", "1");
            $("#div-pag").css("opacity", "1");
            // $("#spin").hide();
            // $("#table_tbody").show();
        }),
        error: (function (e) {
            alert('Error');
        })
    });
}

function handleDelete(id) {
    Swal.fire({
        title: 'この都道府県の医療機関を削除してもよろしいですか。',
        text: "元に戻すことはできません。",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'はい。削除します。'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: 'DELETE',
                url: `/api/medicalInstitution?id=${id}`,
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                headers: {
                    Authorization: "Basic " + btoa(userName + ":" + password)
                },
                success: (function () {
                    Swal.fire({
                        position: 'mid',
                        icon: 'success',
                        title: '削除が完了します。',
                        showConfirmButton: false,
                        timer: 1500
                    })
                }),
                error: (function (e) {
                    Swal.fire({
                        position: 'mid',
                        icon: 'error',
                        title: '削除はできませんでした。',
                        showConfirmButton: false,
                        timer: 1500
                    })
                })
            });
        }
    })
}

function handleSearch() {
    $("#table_tbody").css("opacity", "0.5");
    $("#div-pag").css("opacity", "0.5");
    $("#div_nodata").css("display", "none");
    // $("#table_tbody").hide();
    // $("#spin").show();
    name = $("#pre-name").val().trim();
    $.ajax({
        type: 'GET',
        url: `/api/prefecture?page=${page}&limit=${limit}&name=${name}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function (result) {
            if (result.data.length > 0) {
                let i = 1;
                let attribute = ""
                for (const value of result.data) {
                    attribute +=
                        `<tr id="${value.id}">
                    <td>${i}</td>
                    <td style="text-align: center">${value.name}</td>
                    <td style="text-align: center">${value.nameKana}</td>
                    <td style="text-align: center">
                        <button type="button" class="btn-action" id="btnAction" onclick="handleDelete(${value.id})">Action</button>
                    </td>
                </tr>`;
                    i++;
                }
                $("#table_tbody").html(attribute);
                let attribute2 = `<li><a id="previous" onclick="loadData(0)">«</a></li>`;
                for (let j = 0; j < result.totalPage; j++) {
                    if (j === page) {
                        attribute2 += `<li><a id="active${j}" onclick="loadData(${j})" class="active">${j + 1}</a></li>`;
                    } else {
                        attribute2 += `<li><a id="active${j}" onclick="loadData(${j})">${j + 1}</a></li>`;
                    }
                }
                $("#pagination").html(attribute2);
                $("#pagination").append(`<li><a id="next" onclick="loadData(${result.totalPage - 1})">»</a></li>`);
                $("#div-pag").show();
                $("#table_tbody").show();
                $("#table_tbody").css("opacity", "1");
                $("#div-pag").css("opacity", "1");
            } else {
                $("#table_tbody").hide();
                $("#div-pag").hide();
                $("#div_nodata").css("display", "block");
            }
        }),
        error: (function (e) {
            alert('Error');
        })
    });
}


function handleSelect(i) {
    $("#pre-name").val("")
    let attribute = "";
    if (i === 0) {
        valueRadio = 0;
        attribute = `<tr>
                <th>No.</th>
                <th style="text-align: center">都道府県名</th>
                <th style="text-align: center">都道府県カナ</th>
                <th style="text-align: center">Action</th>
            </tr>`;
        $("#table_thead").html(attribute);
        $("#table_tbody").html("");
        $("#pagination").html("");
        handleSearch();
    } else {
        valueRadio = 1;
        attribute = `<tr>
                <th>No.</th>
                <th style="text-align: center">都道府県</th>
                <th style="text-align: center">区分</th>
                <th style="text-align: center">医療機関名</th>
                <th style="text-align: center">医療機関コード</th>
                <th style="text-align: center">郵便番号</th>
                <th style="text-align: center">所在地</th>
                <th style="text-align: center">電話番号</th>
            </tr>`;
        $("#table_thead").html(attribute);
        $("#table_tbody").html("");
        $("#pagination").html("");
    }
}

function removeFile(e) {
    e.parentNode.remove();
    for (let i = 0; i < listFile.length; i++) {
        if (e.id === listFile[i].name) {
            listFile.splice(i, 1);
        }
    }
}

function choseFiles() {
    $("#file-select").hide();
    let input = document.getElementById('files');
    let output = document.getElementById('fileList');
    let children = "";
    for (let i = 0; i < input.files.length; i++) {
        listFile.push(input.files.item(i));
        children += `<li>${input.files.item(i).name}<span class="remove-list" onclick="removeFile(this)" id="${input.files.item(i).name}">X</span></li>`
    }
    // for (let i = 0; i < listFile.length; i++) {
    //     children +=  `<li>${listFile[i].item(i).name}<span class="remove-list" onclick="removeFile(this)" id="${i}">X</span></li>`
    // }
    output.innerHTML = children;
    $("#style-3").css("border", "none");
};

function upload() {
    if (listFile.length > 0) {
        $("#style-3").css("border", "none");
        for (const file of listFile) {
            formData.append("files", file);
        }
        $.ajax({
            type: 'POST',
            url: `/api/medicalInstitution/uploads`,
            processData: false,
            contentType: false,
            data: formData,
            success: (function (result) {
                Swal.fire({
                    position: 'mid',
                    icon: 'success',
                    title: 'アップロードが完了しました。',
                    showConfirmButton: false,
                    timer: 1500
                })
            }),
            error: (function (e) {
                Swal.fire({
                    position: 'mid',
                    icon: 'error',
                    title: 'アップロードができませんでした。',
                    showConfirmButton: false,
                    timer: 1500
                })
            })
        });
    } else {
        $("#style-3").css("border", "1px solid red");
    }
}