メモというか残骸があったのでこちらに残しておいた

viewをまとめて管理するためのものがview group
- LinearLayout
パーツを縦一列もしくは横一列に並べる場合に使用するレイアウト


- relativelayout
相対的に指定できる
- table
- frame
viewをかせてて配置できる


viewの横幅
android:layout_width
このように表現するらしいので、そういうものだとして扱う

viewの中の要素を上下左右に移動させるためのもの
android:gravity

親要素に対して寄せる
android:layout_gravity

viewの指定
- match parent は親要素の幅に合わせるという意味、
- wrap_content は中身の幅に合わせるという意味
ボタンでいうと中の文字列に合わせて大きさを変える

xmlnsは名前空間の指定

# LinearLayout
orientationで縦に揃えるのか横に揃えるのかを指定している
android:orientation="vertical"

寄せ方
viewの中の要素を上下左右に移動させるためのもの
→ボタンの中身の文字を下にするとか
android:gravity

親要素に対して寄せる
→そもそもボタンの位置を下にするとか
android:layout_gravity

viewの指定
 - match parent は親要素の幅に合わせるという意味、
  - wrap_content は中身の幅に合わせるという意味
  ボタンでいうと中の文字列に合わせて大きさを変える


  layout_weight 1:1:3とかの表現をする
  余白の割り当て

# RelativeLayout
位置指定の仕方
android:layout_centerInParent="true"

# FrameLayout
viewを重ねる

# TableLayout
計算機みたいなボタンをこれで作れる
ボタンを一つにまとめるとか、横幅を大きくする、小さくするとかもできるので必要があれば検索
<TableRow>
　　<Button />
　　<Button />
　　<Button />
</TableRow>


# リストビューを設置しよう
幅と高さは必ずmatch_parentにする

