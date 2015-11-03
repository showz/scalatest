# scalatestアプリの説明

## GraphQLの使い方（起動してる前提）
※まだ UserのID指定しか出来ません
### GETの場合
```
http://192.168.37.11/graphql?query=query User { User (id: 1) { name } }
```
### POSTの場合
POSTのBodyに
```
{
  "query" : "query User { User (id: 1) { name } }"
}
```
