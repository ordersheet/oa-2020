
var resourcesIds;
$(function () {
    getMenuTreeData();
    validateRule();
});
$.validator.setDefaults({
    submitHandler: function () {
        getAllSelectNodes();
        save();
    }
});

function getAllSelectNodes() {
    var ref = $('#menuTree').jstree(true); // 获得整个树
    resourcesIds = ref.get_selected(); // 获得所有选中节点的，返回值为数组
    $("#menuTree").find(".jstree-undetermined").each(function (i, element) {
        resourcesIds.push($(element).closest('.jstree-node').attr("id"));
    });
}
function getMenuTreeData() {
    $.ajax({
        type: "GET",
        url: "/sys/resources/alltree",
        success: function (menuTree) {
            loadMenuTree(menuTree);
        }
    });
}

function loadMenuTree(menuTree) {
    $('#menuTree').jstree({
        'core': {
            'data': menuTree
        },
        "checkbox": {
        },
        "plugins": ["wholerow", "checkbox"]
    });
    $('#menuTree').jstree("open_all");
}

function save() {
    $('#resourcesIds').val(resourcesIds);
    var role = $('#signupForm').serialize();
    $.ajax({
        cache: true,
        type: "post",
        url: "/sys/role/save",
        data: role, // 你的formid
        async: false,
        error: function (request) {
            alert("Connection error");
        },
        success: function (data) {
            console.assert(data);
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                alert(parent);
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);

            } else {
                parent.layer.msg(data.msg);
            }
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            roleName: {
                required: true
            }
        },
        messages: {
            roleName: {
                required: icon + "请输入角色名"
            }
        }
    });
}