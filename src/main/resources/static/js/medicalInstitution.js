let page = 0;
let limit = 10;
let request = "";
let currentPage = 0;
let totalPage = 0;
const userName = "admin";
const password = "crawler123";
let status = 0;
init();

function init() {
    $.ajax({
        type: 'GET',
        url: `/api/medicalInstitution/search?page=${page}&limit=${limit}&request=${request}&status=${status}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function (result) {
            if (result.data.length > 0) {
                totalPage = result.totalPage;
                let i = 1;
                let attribute = "";
                for (const value of result.data) {
                    attribute +=
                        `<tr id="${value.id}">
                            <td style="vertical-align: middle">${i}</td>
                            <td style="text-align: center; vertical-align: middle">${value.prefectureName}</td>
                            <td style="text-align: center; vertical-align: middle">${value.categoryName}</td>
                            <td style="text-align: left; vertical-align: middle">${value.name}</td>
                            <td style="text-align: center; vertical-align: middle;">
                                <a href="/medicalInstitutionDetail?id=${value.id}">
                                    ${value.code}
                                </a>
                            </td>
                            <td style="text-align: center; vertical-align: middle">${value.post}</td>
                            <td style="text-align: left; vertical-align: middle">${value.address}</td>
                            <td style="text-align: center; vertical-align: middle">${value.phone}</td>
                            <td style="text-align: center; vertical-align: middle">
                                <a href="/medicalInstitutionDetail?id=${value.id}" id="status${value.id}" style="margin: 0px; color: white; border-radius: 4px; padding: 6px; background-color: #17a2b8">
                                詳細
                                </a>
                            </td>
                        </tr>`;
                    i++;
                }
                ;
                $("#table_tbody").html(attribute);
                pagination(0, result.totalPage);
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
    $("a").removeClass("active");
    $("#active" + page).addClass("active");
    currentPage = page;
    $.ajax({
        type: 'GET',
        url: `/api/medicalInstitution/search?page=${page}&limit=${limit}&request=${request}&status=${status}`,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        headers: {
            Authorization: "Basic " + btoa(userName + ":" + password)
        },
        success: (function (result) {
            let i = 1;
            let attribute = ""
            for (const value of result.data) {
                attribute +=
                    `<tr id="${value.id}">
                    <td style="vertical-align: middle">${i}</td>
                    <td style="text-align: center; vertical-align: middle">${value.prefectureName}</td>
                    <td style="text-align: center; vertical-align: middle">${value.categoryName}</td>
                    <td style="text-align: left; vertical-align: middle">${value.name}</td>
                    <td style="text-align: center; vertical-align: middle">
                        <a href="/medicalInstitutionDetail?id=${value.id}">
                            ${value.code}
                        </a>
                    </td>
                    <td style="text-align: center; vertical-align: middle">${value.post}</td>
                    <td style="text-align: left; vertical-align: middle">${value.address}</td>
                    <td style="text-align: center; vertical-align: middle">${value.phone}</td>
                    <td style="text-align: center; vertical-align: middle">
                        <a href="/medicalInstitutionDetail?id=${value.id}" id="status${value.id}" style="margin: 0px; color: white; border-radius: 4px; padding: 6px; background-color: #17a2b8">
                        詳細
                        </a>
                    </td>
                </tr>`;
                i++;
            }
            $("#table_tbody").html(attribute);
            if (totalPage > 10) {
                pagination(page, totalPage);
            }
            $("#table_tbody").css("opacity", "1");
            $("#div-pag").css("opacity", "1");
        }),
        error: (function (e) {
            alert('Error');
        })
    });

}

function handleSearch() {
    $("#table_tbody").css("opacity", "0.5");
    $("#div-pag").css("opacity", "0.5");
    $("#div_nodata").css("display", "none");
    // $("#table_tbody").hide();
    // $("#spin").show();
    request = $("#pre-name").val();
    $.ajax({
        type: 'GET',
        url: `/api/medicalInstitution/search?page=${page}&limit=${limit}&request=${request}&status=${status}`,
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
                    <td style="text-align: center; vertical-align: middle">${value.prefectureName}</td>
                    <td style="text-align: center; vertical-align: middle">${value.categoryName}</td>
                    <td style="text-align: left; vertical-align: middle">${value.name}</td>
                    <td style="text-align: center; vertical-align: middle">
                        <a href="/medicalInstitutionDetail?id=${value.id}">
                            ${value.code}
                        </a>
                    </td>
                    <td style="text-align: center; vertical-align: middle">${value.post}</td>
                    <td style="text-align: left; vertical-align: middle">${value.address}</td>
                    <td style="text-align: center; vertical-align: middle">${value.phone}</td>
                    <td style="text-align: center; vertical-align: middle">
                        <a href="/medicalInstitutionDetail?id=${value.id}" id="status${value.id}" style="margin: 0px; color: white; border-radius: 4px; padding: 6px; background-color: #17a2b8">
                        詳細
                        </a>
                    </td>
                </tr>`;
                    i++;
                }
                $("#table_tbody").html(attribute);
                totalPage = result.totalPage;
                if (result.totalPage > 10) {
                    pagination(0, result.totalPage);
                } else {
                    let attribute2 = `<li><a id="first" onclick="loadData(0)"><<</a></li>
<li><a id="previous" onclick="nextOrPre(0)"><</a></li>`;
                    for (let j = 0; j < result.totalPage; j++) {
                        if (j === page) {
                            attribute2 += `<li><a id="active${j}" onclick="loadData(${j})" class="active">${j + 1}</a></li>`;
                        } else {
                            attribute2 += `<li><a id="active${j}" onclick="loadData(${j})">${j + 1}</a></li>`;
                        }
                    }
                    $("#pagination").html(attribute2);
                    $("#pagination").append(`<li><a id="next" onclick="nextOrPre(1)">>></a></li>
<li><a id="last" onclick="loadData(${totalPage - 1})">>></a></li>`);
                }
                $("#div-pag").show();
                $("#table_tbody").show();
                $("#table_tbody").css("opacity", "1");
                $("#div-pag").css("opacity", "1");
                $("#title_desc").html("Search to display data");
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

function pagination(currentPage, totalPage) {
    let c = currentPage;
    if (totalPage > 10 && currentPage + 1 <= totalPage - 10) {
        if (currentPage == 0) {
            c = currentPage + 1;
        }
        let attribute = `<li><a id="first" onclick="loadData(0)"><<</a></li><li><a id="previous" onclick="loadData(${currentPage === 0 ? 0 : (currentPage - 1)})"><</a></li>`;
        for (let i = c - 1; i < currentPage + 10; i++) {
            if (i === currentPage) {
                attribute += `<li><a id="active${i}" onclick="loadData(${i})" class="active">${i + 1}</a></li>`;
            } else {
                attribute += `<li><a id="active${i}" onclick="loadData(${i})">${i + 1}</a></li>`;
            }
        }
        attribute += `<li><a>...</a></li>`;
        attribute += `<li><a id="active${totalPage - 1}" onclick="loadData(${totalPage - 1})">${totalPage}</a></li>
<li><a id="next" onclick="loadData(${currentPage === (totalPage - 1) ? (totalPage - 1) : (currentPage + 1)})">></a></li>
<li><a id="last" onclick="loadData(${totalPage - 1})">>></a></li>`;
        $("#pagination").html(attribute);
    } else if (totalPage > 10 && currentPage + 1 > totalPage - 10) {
        let attribute = `<li><a id="first" onclick="loadData(0)"><<</a></li><li><a id="previous" onclick="loadData(${currentPage === 0 ? 0 : (currentPage - 1)})"><</a></li>`;
        for (let i = totalPage - 10; i <= totalPage - 1; i++) {
            if (i === currentPage) {
                attribute += `<li><a id="active${i}" onclick="loadData(${i})" class="active">${i + 1}</a></li>`;
            } else {
                attribute += `<li><a id="active${i}" onclick="loadData(${i})">${i + 1}</a></li>`;
            }
        }
        attribute += `<li><a id="next" onclick="loadData(${currentPage === (totalPage - 1) ? (totalPage - 1) : (currentPage + 1)})">></a></li>
<li><a id="last" onclick="loadData(${totalPage - 1})">>></a></li>`;
        $("#pagination").html(attribute);
    } else if (totalPage < 10) {
        let attribute = `<li><a id="first" onclick="loadData(0)"><<</a></li><li><a id="previous" onclick="loadData(${currentPage === 0 ? 0 : (currentPage - 1)})"><</a></li>`;
        for (let i = 0; i < totalPage; i++) {
            if (i === currentPage) {
                attribute += `<li><a id="active${i}" onclick="loadData(${i})" class="active">${i + 1}</a></li>`;
            } else {
                attribute += `<li><a id="active${i}" onclick="loadData(${i})">${i + 1}</a></li>`;
            }
        }
        attribute += `<li><a id="next" onclick="loadData(${currentPage === (totalPage - 1) ? (totalPage - 1) : (currentPage + 1)})">></a></li>
<li><a id="last" onclick="loadData(${totalPage - 1})">>></a></li>`;
        $("#pagination").html(attribute);
    }
}

async function handleSelectSearch() {
    if ($('#select-search').val() != status) {
        status = $('#select-search').val();
        if (status == 0) {
            $("#pre-name").attr("placeholder", "都道府県名で検索");
        } else {
            $("#pre-name").attr("placeholder", "コードで検索");
        }
        $("#pre-name").val("");
        request = "";
        await init();
        $("#div-pag").show();
        $("#table_tbody").show();
        $("#table_tbody").css("opacity", "1");
        $("#div-pag").css("opacity", "1");
        $("#div_nodata").css("display", "none");
    }
    console.log(status, " - ", $('#select-search').val())

}

function nextOrPre(type) {
    if (type == 0) {
        loadData(currentPage === 0 ? 0 : (currentPage - 1));
    } else {
        loadData(currentPage === (totalPage - 1) ? (totalPage - 1) : (currentPage + 1));
    }
}