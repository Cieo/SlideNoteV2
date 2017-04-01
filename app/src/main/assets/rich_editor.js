/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//function js_getDPI() {
//    var arrDPI = new Array;
//    if (window.screen.deviceXDPI) {
//        arrDPI[0] = window.screen.deviceXDPI;
//        arrDPI[1] = window.screen.deviceYDPI;
//    }
//    else {
//        var tmpNode = document.createElement("DIV");
//        tmpNode.style.cssText = "width:1in;height:1in;position:absolute;left:0px;top:0px;z-index:99;visibility:hidden";
//        document.body.appendChild(tmpNode);
//        arrDPI[0] = parseInt(tmpNode.offsetWidth);
//        arrDPI[1] = parseInt(tmpNode.offsetHeight);
//        tmpNode.parentNode.removeChild(tmpNode);
//    }
//    return arrDPI[0]/160;
//}

var RE = {};

RE.currentSelection = {
    "startContainer": 0,
    "startOffset": 0,
    "endContainer": 0,
    "endOffset": 0};

RE.editor = document.getElementById('editor');
RE.caption = document.getElementById('caption');

/*** *** *** *** *** *** ***********  图片长按-识别事件  *** *** *** *** *** *** ********/
//function ininin(){
//    $('#editor').prepend("<img src='Img/btn_Identity_h_mdpi.png' /><br>");
//}
var ImageUrl = null;
var editorTop = $('#editor').offset().top;
var timeout = 0;
var lastTime=null;
var nowTime=null;
var state = 0;
var start_x=null, start_y=null, move_x=null, move_y=null;
var EditorStartListener = document.querySelector("#editor");
var EditorEndListener = document.querySelector("body");
var EditorMoveListener = document.querySelector("#editor");
var editedImg = null;
EditorStartListener.addEventListener("touchstart", showBox);
EditorEndListener.addEventListener("touchend", hideBox);
EditorMoveListener.addEventListener("touchmove", returnEvent);

$('#Identify').click(function(){
    $('#ImageIdentifyBox').hide();
    state = 0;
    $("#editor").attr('contentEditable','true');
    document.removeEventListener('touchmove', handler, false);
    console.log("图片路径："+ImageUrl);
    window.imageListener.actionFromJsWithParam(ImageUrl);
});

$('#Delete').click(function(){
    var arr = new Array();
    arr = ImageUrl.split('/');
    var image_url_string = arr[arr.length-1];
    $("img[src$='"+image_url_string+"']").remove();
    $('#ImageIdentifyBox').hide();
    state = 0;
    $("#editor").attr('contentEditable','true');
    document.removeEventListener('touchmove', handler, false);
});
function handler(){
    event.preventDefault();
}
function showBox(event){

    if (event.target.tagName.toLowerCase() == "img"){
        window.imageListener.clickImage();
        console.log(event.currentTarget.getAttribute('contenteditable'));
        event.currentTarget.setAttribute('contenteditable', false);
        console.log(event.currentTarget.getAttribute('contenteditable'));
        start_x = event.touches[0].clientX;
        start_y = event.touches[0].clientY;
        lastTime = new Date().getTime();
        editedImg = event.target;
//        document.addEventListener('touchmove',handler,false);// 阻止触摸时浏览器的缩放、滚动条滚动
    }
    else{
        $('#ImageIdentifyBox').hide();
        state = 0;
    }
    //console.log("----------------------start:"+$("#editor").attr("contentEditable")+state);
}
function returnEvent(event){
    if (event.target.tagName.toLowerCase() == "img") {
        //console.log('aaa');
        move_x = event.changedTouches[0].clientX;// 获取第一个触点
        move_y = event.changedTouches[0].clientY;
        if (move_x - start_x > 30 || move_x - start_x < -30
            || move_y - start_y > 30 || move_y - move_y < -30) {
            state = 1;
            document.removeEventListener('touchmove', handler, false);
            //console.log("----------------------move:"+"滑动了"+state);
        }
    }
}
function hideBox(event){
    //console.log('ccc');
    if (event.target.tagName.toLowerCase() == "img"){
        var obj = $(event.target);
        // 获取点击图片的路径
        ImageUrl = obj[0].src;
        nowTime = new Date().getTime();
        window.imageListener.endClickImage();
        var timeLength = nowTime - lastTime;
        if(timeLength >= 300 && state == 0){
            var curLeft = obj .offset().left;
            var curTop = obj .offset().top + obj .outerHeight();
            curLeft = curLeft + obj.width()*0.8 - 10;
            curTop = curTop - editorTop - 40;
            $('#ImageIdentifyBox').css('top',curTop+"px").css('left', curLeft+"px").show();
            state = 1;
        }
        else{
            $('#ImageIdentifyBox').hide();
            state = 0;
            $("#editor").attr('contentEditable','true');
        }
        //console.log("----------------------end:"+$("#editor").attr("contentEditable")+state);
    }
}

function updateImage(newPath) {
    console.log('newPath '+newPath);
    if (editedImg != null) {
        editedImg.src = 'file://'+ newPath;
    }
}

/*********************************  End  *********************************/

document.addEventListener("selectionchange", function() { RE.backuprange(); });

/*********************************  接口  *********************************/

RE.getTitleContents = function(){
    window.imageListener.setTitleContents(document.getElementById('title').innerText);
}
RE.getHTMLContents = function(){
    window.imageListener.setHTMLContents(document.getElementsByTagName('html')[0].innerHTML);
}
RE.getDateContents = function(){
    window.imageListener.setDateContents(document.getElementById('createDate').innerText);
}
RE.getEditorContents = function(){
	window.imageListener.setEditorContents(RE.editor.innerHTML);
}
/***************************  接口END  ******************************/

/*******************************  设置    *****************************/
RE.setEditorContents = function(contents){
    RE.editor.innerHTML = contents;
}
RE.setTitle = function(contents){
    $('#title').text(contents);
}
RE.setDate = function(contents){
    $('#createDate').text(contents);
}
RE.getImageUrl = function(){
    return ImageUrl;
}
// Initializations
RE.callback = function() {
    window.location.href = "re-callback://" + encodeURI(RE.getHtml());
}

RE.setHtml = function(contents) {
    RE.editor.innerHTML = decodeURIComponent(contents.replace(/\+/g, '%20'));
}

RE.getHtml = function() {
    return RE.caption.innerHTML+RE.editor.innerHTML;
}

RE.getText = function() {
    return RE.caption.innerText+RE.editor.innerText;
}

RE.setBaseTextColor = function(color) {
    RE.editor.style.color  = color;
}

RE.setBaseFontSize = function(size) {
    RE.editor.style.fontSize = size;
}

RE.setPadding = function(left, top, right, bottom) {
    RE.editor.style.paddingLeft = left;
    RE.editor.style.paddingTop = top;
    RE.editor.style.paddingRight = right;
    RE.editor.style.paddingBottom = bottom;
}

RE.setBackgroundColor = function(color) {
    document.body.style.backgroundColor = color;
}

RE.setBackgroundImage = function(image) {
    RE.editor.style.backgroundImage = image;
}

RE.setWidth = function(size) {
    RE.editor.style.minWidth = size;
}

RE.setHeight = function(size) {
    RE.editor.style.height = size;
}

RE.setTextAlign = function(align) {
    RE.editor.style.textAlign = align;
}

RE.setVerticalAlign = function(align) {
    RE.editor.style.verticalAlign = align;
}

RE.setPlaceholder = function(placeholder) {
    RE.editor.setAttribute("placeholder", placeholder);
}

RE.undo = function() {
    document.execCommand('undo', false, null);
}

RE.redo = function() {
    document.execCommand('redo', false, null);
}

RE.setBold = function() {
    document.execCommand('bold', false, null);
}

RE.setItalic = function() {
    document.execCommand('italic', false, null);
}

RE.setSubscript = function() {
    document.execCommand('subscript', false, null);
}

RE.setSuperscript = function() {
    document.execCommand('superscript', false, null);
}

RE.setStrikeThrough = function() {
    document.execCommand('strikeThrough', false, null);
}

RE.setUnderline = function() {
    document.execCommand('underline', false, null);
}

RE.setBullets = function() {
    document.execCommand('insertUnorderedList', false, null);
}

RE.setNumbers = function() {
    document.execCommand('insertOrderedList', false, null);
}

RE.setTextColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('foreColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setTextBackgroundColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('hiliteColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setFontSize = function(fontSize){
    document.execCommand("fontSize", false, fontSize);
}

RE.setHeading = function(heading) {
    document.execCommand('formatBlock', false, '<h'+heading+'>');
}

RE.setIndent = function() {
    document.execCommand('indent', false, null);
}

RE.setOutdent = function() {
    document.execCommand('outdent', false, null);
}

RE.setJustifyLeft = function() {
    document.execCommand('justifyLeft', false, null);
}

RE.setJustifyCenter = function() {
    document.execCommand('justifyCenter', false, null);
}

RE.setJustifyRight = function() {
    document.execCommand('justifyRight', false, null);
}

RE.setBlockquote = function() {
    document.execCommand('formatBlock', false, '<blockquote>');
}

RE.insertImage = function(url, alt) {
    var html = '<img src="' + url + '" alt="' + alt + '" /><br><br>';
    RE.insertHTML(html);
}

RE.insertHTML = function(html) {
    RE.restorerange();
    document.execCommand('insertHTML', false, html);
}

RE.insertLink = function(url, title) {
    RE.restorerange();
    var sel = document.getSelection();
    if (sel.toString().length == 0) {
        document.execCommand("insertHTML",false,"<a href='"+url+"'>"+title+"</a>");
    } else if (sel.rangeCount) {
        var el = document.createElement("a");
        el.setAttribute("href", url);
        el.setAttribute("title", title);

        var range = sel.getRangeAt(0).cloneRange();
        range.surroundContents(el);
        sel.removeAllRanges();
        sel.addRange(range);
    }
    RE.callback();
}

RE.setTodo = function(text) {
    var html = '<input type="checkbox" name="'+ text +'" value="'+ text +'"/> &nbsp;';
    document.execCommand('insertHTML', false, html);
}

RE.prepareInsert = function() {
    RE.backuprange();
}

RE.backuprange = function(){
    var selection = window.getSelection();
    if (selection.rangeCount > 0) {
        var range = selection.getRangeAt(0);
        RE.currentSelection = {
            "startContainer": range.startContainer,
            "startOffset": range.startOffset,
            "endContainer": range.endContainer,
            "endOffset": range.endOffset};
    }
}

RE.restorerange = function(){
    var selection = window.getSelection();
    selection.removeAllRanges();
    var range = document.createRange();
    range.setStart(RE.currentSelection.startContainer, RE.currentSelection.startOffset);
    range.setEnd(RE.currentSelection.endContainer, RE.currentSelection.endOffset);
    selection.addRange(range);
}

RE.enabledEditingItems = function(e) {
    var items = [];
    if (document.queryCommandState('bold')) {
        items.push('bold');
    }
    if (document.queryCommandState('italic')) {
        items.push('italic');
    }
    if (document.queryCommandState('subscript')) {
        items.push('subscript');
    }
    if (document.queryCommandState('superscript')) {
        items.push('superscript');
    }
    if (document.queryCommandState('strikeThrough')) {
        items.push('strikeThrough');
    }
    if (document.queryCommandState('underline')) {
        items.push('underline');
    }
    if (document.queryCommandState('insertOrderedList')) {
        items.push('orderedList');
    }
    if (document.queryCommandState('insertUnorderedList')) {
        items.push('unorderedList');
    }
    if (document.queryCommandState('justifyCenter')) {
        items.push('justifyCenter');
    }
    if (document.queryCommandState('justifyFull')) {
        items.push('justifyFull');
    }
    if (document.queryCommandState('justifyLeft')) {
        items.push('justifyLeft');
    }
    if (document.queryCommandState('justifyRight')) {
        items.push('justifyRight');
    }
    if (document.queryCommandState('insertHorizontalRule')) {
        items.push('horizontalRule');
    }
    var formatBlock = document.queryCommandValue('formatBlock');
    if (formatBlock.length > 0) {
        items.push(formatBlock);
    }

    window.location.href = "re-state://" + encodeURI(items.join(','));
}

RE.focus = function() {
    var range = document.createRange();
    range.selectNodeContents(RE.editor);
    range.collapse(false);
    var selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
    RE.editor.focus();
}

RE.blurFocus = function() {
    RE.editor.blur();
}

RE.removeFormat = function() {
    execCommand('removeFormat', false, null);
}

// Event Listeners
RE.editor.addEventListener("input", RE.callback);
RE.editor.addEventListener("keyup", function(e) {
    var KEY_LEFT = 37, KEY_RIGHT = 39;
    if (e.which == KEY_LEFT || e.which == KEY_RIGHT) {
        RE.enabledEditingItems(e);
    }
});
RE.editor.addEventListener("click", RE.enabledEditingItems);