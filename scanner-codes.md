# Scanner code documentation
Scanner request/responses are encoded in 64-byte string tokens beginning with a
four character code. All available codes are provided in the `utsushi` source
file `utsushi/drivers/esci/code-token.hpp` but their meaning appears to be
undocumented. This is a list of tokens with their inferred meanings from
examining the source code.

Tokens are grouped into namespaces (sections) in the source according to their
general function. The grouping has been preserved here.

## Request Tokens

| Token | Definition                                        |
| ----- | ------------------------------------------------- |
| FIN   | |
| CAN   | Cancel command |
| INFO  | Get scanner information |
| CAPA  | Get scanner capabilities |
| CAPB  | |
| PARA  | Parameters |
| PARB  | |
| RESA  | |
| RESB  | |
| STAT  | Get scanner status |
| MECH  | Send mechanical command (below) |
| TRDT  | |
| IMG   | Scan command |
| EXT0  | |
| EXT1  | |
| EXT2  | |


