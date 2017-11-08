KindEditor.ready(function (K) {
    window.editor = K.create("#description",{
        uploadJson : '../../image_upload.action',
        fileManagerJson : '../../image_manage.action',
        allowFileManager : true
    });

});