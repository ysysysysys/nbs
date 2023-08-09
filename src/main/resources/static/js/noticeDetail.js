window.addEventListener('load',function(){

                if (document.getElementById("sent").value == "DONE") {
                     document.getElementById("attendanceCheck").disabled = true;
                }

            <!--  console.log("テスト用：F12のconsoleに出力");-->

            });

            // 「参加する」のチェックボックスを取得
            const attendanceCheckbox = document.getElementById("attendanceCheck");
            // 送信ボタンを取得
            const submitBtn = document.getElementById("submit-btn");

            // チェックボックスをクリックした時
            attendanceCheckbox.addEventListener("change", () => {
              // チェックされている場合
              if (attendanceCheckbox.checked === true) {
                submitBtn.disabled = false; // disabledを外す
              }
              // チェックされていない場合
              else {
                submitBtn.disabled = true; // disabledを付与
              }
            });