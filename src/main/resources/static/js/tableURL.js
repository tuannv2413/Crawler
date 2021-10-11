let page = 0;
let limit = 10;
let name = "";
let valueId = 0;
let currentPage = 0;
let totalPage = 0;
let totalItems = 0;
let urlId = 0;
let urlText = "";
const userName = "admin";
const password = "crawler123";
init();

function init() {
    $("#table_tbody").hide();
    $("#div-pag").hide();
    $("#spin").show();
    $.ajax({
        type: 'GET',
        url: `/api/urls?page=${page}&limit=${limit}`,
        contentType: 'application/json; charset=utf-8',
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        dataType: 'json',
        success: (function (result) {
            if (result.data.length > 0) {
                totalItems = result.totalItems;
                totalPage = result.totalPage;
                let i = 1;
                let attribute = "";
                for (const value of result.data) {
                    attribute +=
                        `<tr id="${value.id}">
                        <td style="vertical-align: middle">${i}</td>
                        <td style="text-align: center;vertical-align: middle">${value.regionName}</td>
                        <td style="text-align: center;vertical-align: middle">${value.categoryName}</td>
                        <td style="text-align: left;word-break: break-word; vertical-align: middle" id="url${i}">
                            <a href="${value.url}">${value.url}</a>
                        </td>
                        <td style="text-align: center; font-weight: bold; vertical-align: middle">
                            <p id="status${value.id}" style="margin: 0px; color: white; border-radius: 4px; padding: 6px; ${value.status === 1 ? "background-color: #dc3545" : value.status === 2 ? "background-color: #17a2b8" : value.status === 3 ? "background-color: #28a745" : "background-color: black"}">
                                ${value.status === 1 ? "失敗" : value.status === 2 ? "処理中" : value.status === 3 ? "完了" : "未実施"}
                            </p>
                       </td>
                        <td style="text-align: center; vertical-align: middle">
                            <button type="button" class="btn-action" id="btnEdit" onclick="handleUpdate(${value.id}, ${"'" + value.url + "'"})">更新</button>
                        </td>
                        <td style="text-align: center; vertical-align: middle">
                            <button type="button" class="btn-action" id="btnDelete" onclick="handleDelete(${value.id})">削除</button>
                        </td>
                    </tr>`;
                    i++;
                }
                ;
                $("#table_tbody").html(attribute);
                pagination(0,result.totalPage);
                $("#spin").hide();
                $("#table_tbody").show();
                $("#div-pag").show();
                $("#table_tbody").css("opacity", "1");
                $("#div-pag").css("opacity", "1");
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
    currentPage = page;
    $("a").removeClass("active");
    $("#active" + page).addClass("active");
    $.ajax({
        type: 'GET',
        url: `/api/urls?page=${page}&limit=${limit}&name=${name}`,
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
                        <td style="vertical-align: middle">${i}</td>
                        <td style="text-align: center; vertical-align: middle">${value.regionName}</td>
                        <td style="text-align: center; vertical-align: middle">${value.categoryName}</td>
                        <td style="text-align: left; vertical-align: middle" id="url${i}">
                            <a href="${value.url}">${value.url}</a>
                        </td>
                        <td style="text-align: center; font-weight: bold; vertical-align: middle">
                            <p id="status${value.id}" style="margin: 0px; color: white; border-radius: 4px; padding: 6px; ${value.status === 1 ? "background-color: #dc3545" : value.status === 2 ? "background-color: #17a2b8" : value.status === 3 ? "background-color: #28a745" : "background-color: black"}">
                            ${value.status === 1 ? "失敗" : value.status === 2 ? "処理中" : value.status === 3 ? "完了" : "未実施"}
                            </p>
                       </td>
                        <td style="text-align: center; vertical-align: middle">
                            <button type="button" class="btn-action" id="btnEdit" onclick="handleUpdate(${value.id}, ${"'" + value.url + "'"})" >更新</button>
                        </td>
                        <td style="text-align: center; vertical-align: middle">
                            <button type="button" class="btn-action" id="btnDelete" onclick="handleDelete(${value.id})">削除</button>
                        </td>
                    </tr>`
                    i++;
                }
                if(page == totalPage){
                    totalPage = page+1;
                }
                pagination(page, totalPage);
                $("#table_tbody").html(attribute);
                $("#table_tbody").css("opacity", "1");
                $("#div-pag").show();
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

function handleDelete(id) {
    Swal.fire({
        title: 'URL削除してもよろしいでしょうか。',
        text: "関連データも削除されますが、よろしいでしょうか。",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        cancelButtonText: 'キャンセル',
        confirmButtonText: 'はい'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: 'DELETE',
                url: `/api/urls?id=${id}`,
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                headers: {
                    Authorization: "Basic " + btoa(userName + ":" + password)
                },
                success: (async function (result) {
                    await Swal.fire({
                        position: 'mid',
                        icon: 'success',
                        title: result.message,
                        showConfirmButton: false,
                        timer: 1500
                    });
                    totalItems = totalItems - 1;
                    if (totalItems % 10 == 0) {
                        if ((currentPage+1) >= totalPage){
                            currentPage = Math.floor(totalItems/10) - 1;
                        }
                        totalPage = totalItems/10;
                    }else if (totalItems % 10 != 0 && (currentPage+1) >= totalPage) {
                        if ((currentPage+1) >= totalPage){
                            currentPage = Math.floor(totalItems/10);
                        }
                        totalPage = Math.floor(totalItems/10) + 1;
                    }
                    //     console.log("OK1", " - ", totalPage, " - ", totalItems, " - ", cp, " - ", currentPage);
                        loadData(currentPage >= 0 ? currentPage : 0);
                }),
                error: (function (e) {
                    Swal.fire({
                        position: 'mid',
                        icon: 'error',
                        title: e.responseJSON.message,
                        showConfirmButton: false,
                        timer: 1500
                    })
                })
            });
        }
    })
}

function handleUpdate(id, url) {
    valueId = id;
    $('#formModal').modal('show');
    $('#url').val(url);
    urlText = url;
}

function handleClickUpdate() {
    if (valueId > 0) {
        let url = escapeHtml($('#url').val());
        $.ajax({
            type: 'PUT',
            url: `/api/urls`,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
                "urlsDto":{"id": valueId, "url": url},
                "url":urlText
            }),
            headers: {
                Authorization: "Basic " + btoa(userName + ":" + password)
            },
            success: (function (result) {
                Swal.fire({
                    position: 'mid',
                    icon: 'success',
                    title: result.message,
                    showConfirmButton: false,
                    timer: 1500
                });
                loadData(currentPage);
                $("#btn-upload1").css('background-color', '#0069d9');
                $("#btn-upload1").prop('disabled', false);
            }),
            error: (function (e) {
                if (e.status === 400) {
                    Swal.fire({
                        position: 'mid',
                        icon: 'error',
                        title: e.responseJSON.message,
                        showConfirmButton: false,
                        timer: 1500
                    })
                }else {
                    Swal.fire({
                        position: 'mid',
                        icon: 'error',
                        title: '更新ができませんでした。',
                        showConfirmButton: false,
                        timer: 1500
                    })
                }
                $("#btn-upload1").css('background-color', '#6c757d');
                $("#btn-upload1").prop('disabled', true);
            })
        });
    }
}

function handleSearchUrl() {
    $("#table_tbody").css("opacity", "0.5");
    $("#div-pag").css("opacity", "0.5");
    $("#div_nodata").css("display", "none");
    let url = escapeHtml($("#url-name").val());
    if (url.trim().length > 0) {
        $.ajax({
            type: 'GET',
            url: `/api/urls/url?url=${url.trim()}`,
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
                        attribute += `<tr id="${value.id}">
                       <td style="vertical-align: middle">${i}</td>
                       <td style="text-align: center; vertical-align: middle">${value.regionName}</td>
                       <td style="text-align: center; vertical-align: middle">${value.categoryName}</td>
                       <td style="text-align: center; vertical-align: middle" id="url${i}">
                          <a href="${value.url}">${value.url}</a>
                       </td>
                       <td style="text-align: center; font-weight: bold; vertical-align: middle">
                            <p id="status${value.id}" style="margin: 0px; color: white; border-radius: 4px; padding: 6px; ${value.status === 1 ? "background-color: #dc3545" : value.status === 2 ? "background-color: #17a2b8" : value.status === 3 ? "background-color: #28a745" : "background-color: black"}">
                            ${value.status === 1 ? "失敗" : value.status === 2 ? "処理中" : value.status === 3 ? "完了" : "未実施"}
                            </p>
                       </td>
                       <td style="text-align: center; vertical-align: middle">
                           <button type="button" class="btn-action" id="btnEdit" onclick="handleUpdate(${value.id}, ${"'" + value.url + "'"})">更新</button>
                       </td>
                       <td style="text-align: center; vertical-align: middle">
                           <button type="button" class="btn-action" id="btnDelete" onclick="handleDelete(${value.id})">削除</button>
                       </td>
                   </tr>`;
                        i++;
                    }
                    $("#table_tbody").html(attribute);
                    $("#div-pag").hide();
                    $("#table_tbody").show();
                    $("#table_tbody").css("opacity", "1");
                } else {
                    $("#table_tbody").hide();
                    $("#div-pag").hide();
                    $("#div_nodata").css("display", "block");
                }
            }),
            error: (function (e) {
                console.log("Error: ", e)
            })
        });
    } else {
        init();
    }
}

function handleShowModal() {
    $('#formModalInsert').modal('show');
};

function handleInsertUrl() {
    $("#div_nodata").css("display", "none");
    $("#table_tbody").show();
    $("#div-pag").show();
    let url = escapeHtml($('#url-text').val());
    if (url.trim().length > 0) {
        $.ajax({
            type: 'POST',
            url: `/api/urls`,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
                "id": 0,
                "url": url
            }),
            headers: {
                Authorization: "Basic " + btoa(userName + ":" + password)
            },
            success: (async function (result) {
                valueId = result.message * 1;
                $("#url-name").val("");
                await Swal.fire({
                    position: 'mid',
                    icon: 'success',
                    title: '保存が完了しました。',
                    showConfirmButton: false,
                    timer: 1500
                });
                totalItems = totalItems + 1;
                if (totalItems % 10 == 0 && totalItems > 10) {
                    loadData(totalItems/10 - 1);
                }else if (totalItems % 10 != 0 && totalItems > 10) {
                    loadData(Math.floor(totalItems/10));
                }else {
                    loadData(currentPage);
                }
                $("#btn-upload").css('background-color', '#0069d9');
                $("#btn-upload").prop('disabled', false);
            }),
            error: (function (e) {
                if (e.status === 400) {
                    Swal.fire({
                        position: 'mid',
                        icon: 'error',
                        title: e.responseJSON.message,
                        showConfirmButton: false,
                        timer: 1500
                    })
                }else {
                    Swal.fire({
                        position: 'mid',
                        icon: 'error',
                        title: '保存ができませんでした。',
                        showConfirmButton: false,
                        timer: 1500
                    })
                }
                $("#btn-upload").css('background-color', '#6c757d');
                $("#btn-upload").prop('disabled', true);
            })
        });
    } else {
        $('#url-text').css("border", "1px solid red");
    }
}

// function upload modal insert
function handleUpload() {
    let url = escapeHtml($('#url-text').val());
    $("#status" + valueId).html("処理中");
    $("#status" + valueId).css({"margin": "0px", "color": "white", "border-radius": "4px", "padding": "6px","background-color": "#17a2b8"});
    $.ajax({
        type: 'POST',
        url: `/api/medicalInstitution/uploads/${valueId}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify({
            "urls": [url]
        }),
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function (result) {
            Swal.fire({
                position: 'mid',
                icon: 'success',
                title: result.message,
                showConfirmButton: false,
                timer: 1500
            })
            $("#btn-upload").css('background-color', '#6c757d');
            $("#btn-upload").prop('disabled', true);
            insertData(result.response*1);
        }),
        error: (function (e) {
            if (e.status === 400) {
                Swal.fire({
                    position: 'mid',
                    icon: 'error',
                    title: e.responseJSON.message,
                    showConfirmButton: false,
                    timer: 1500
                })
            }else{
                Swal.fire({
                    position: 'mid',
                    icon: 'error',
                    title: 'アップロードができませんでした。',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
            $("#btn-upload").css('background-color', '#6c757d');
            $("#btn-upload").prop('disabled', true);
            $("#status" + valueId).html("失敗");
            $("#status" + valueId).css({"margin": "0px", "color": "white", "border-radius": "4px", "padding": "6px","background-color": "#dc3545"});
        })
    });
}

// function upload modal update
function handleUpload1() {
    let url = escapeHtml($('#url').val());
    $("#status" + valueId).html("処理中");
    $("#status" + valueId).css({"margin": "0px", "color": "white", "border-radius": "4px", "padding": "6px","background-color": "#17a2b8"});
    $.ajax({
        type: 'POST',
        url: `/api/medicalInstitution/uploads/${valueId}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify({
            "urls": [url]
        }),
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function (result) {
            Swal.fire({
                position: 'mid',
                icon: 'success',
                title: result.message,
                showConfirmButton: false,
                timer: 1500
            })
            $("#btn-upload1").css('background-color', '#6c757d');
            $("#btn-upload1").prop('disabled', true);
            insertData(result.response*1);
        }),
        error: (function (e) {
            if (e.status === 400) {
                Swal.fire({
                    position: 'mid',
                    icon: 'error',
                    title: e.responseJSON.message,
                    showConfirmButton: false,
                    timer: 1500
                })
            }else{
                Swal.fire({
                    position: 'mid',
                    icon: 'error',
                    title: 'アップロードができませんでした。',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
            $("#btn-upload1").css('background-color', '#6c757d');
            $("#btn-upload1").prop('disabled', true);
            $("#status" + valueId).html("失敗");
            $("#status" + valueId).css({"margin": "0px", "color": "white", "border-radius": "4px", "padding": "6px","background-color": "#dc3545"});
        })
    });
}

function insertData(valueId) {
    $.ajax({
        type: "GET",
        url:`/api/medicalInstitution/new?urlId=${valueId}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function () {
            loadData(currentPage);
        }),
        error: (function (e) {
            console.log(e);
        })
    })
}

function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function pagination(currentPage, totalPage) {
    let attribute = `<li><a id="previous" onclick="loadData(0)"><<</a></li>`;
    for (let j = 0; j < totalPage; j++) {
        if (j === currentPage) {
            attribute += `<li><a id="active${j}" onclick="loadData(${j})" class="active">${j + 1}</a></li>`;
        } else {
            attribute += `<li><a id="active${j}" onclick="loadData(${j})">${j + 1}</a></li>`;
        }
    }
    $("#pagination").html(attribute);
    $("#pagination").append(`<li><a id="next" onclick="loadData(${totalPage - 1})">>></a></li>`);
}
